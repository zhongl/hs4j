package com.github.zhongl.hs4j.kit.proxy;

import static com.github.zhongl.hs4j.kit.util.StringUtils.*;
import static org.apache.commons.codec.digest.DigestUtils.*;

import java.lang.reflect.*;
import java.util.*;

import com.github.zhongl.hs4j.kit.util.*;
import com.google.code.hs4j.*;

/**
 * {@link HandlerSocketProxyFactory}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-2
 * 
 */
public class HandlerSocketProxyFactory extends ProxyFactory {

  public HandlerSocketProxyFactory(HSClient hsClient) {
    this.hsClient = hsClient;
    sessionCache = new HashMap<Object, IndexSession>();
    method2Handler = new HashMap<Method, InvocationHandler>();
  }

  @Override
  protected synchronized InvocationHandler handlerOf(Class<?> clazz) {
    ClassAnnotations classAnnotations = new ClassAnnotations(clazz);
    String database = classAnnotations.getDatabase();
    String table = classAnnotations.getTable();

    for (Method method : clazz.getMethods()) {
      MethodAnnotations methodAnnotations = new MethodAnnotations(method);
      String indexName = methodAnnotations.getIndexNameOrDefault(classAnnotations.getIndexName());
      String[] columns = methodAnnotations.getColumnsOrDefault(classAnnotations.getColumnsOrThrowExceptionWith(method));
      IndexSession indexSession = getOrCreateIndexSessionBy(database, table, indexName, columns);
      InvocationHandler handler = methodAnnotations.createInvocationHandlerWith(indexSession);
      method2Handler.put(method, supportedCastExceptionHandler(handler, method.getExceptionTypes()));
    }
    return dispatcher;
  }

  private InvocationHandler supportedCastExceptionHandler(final InvocationHandler handler, final Class<?>[] exceptionTypes) {
    final Set<Class<?>> exceptionTypeSet = CollectionUtils.set(exceptionTypes);
    final Class<?> firstExceptionType = exceptionTypes.length > 0 ? exceptionTypes[0] : null;
    return new InvocationHandler() {

      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
          return handler.invoke(proxy, method, args);
        } catch (Throwable e) {
          if (!exceptionTypeSet.contains(e.getClass()) && firstExceptionType != null)
            throw (Throwable)firstExceptionType.getConstructor(Throwable.class).newInstance(e);
          throw e;
        }
      }
    };
  }

  private IndexSession getOrCreateIndexSessionBy(String database, String table, String indexName, String[] columns) {
    final Object key = md5Hex(join(indexName, Arrays.toString(columns)));

    IndexSession session = sessionCache.get(key);
    if (session != null) return session;

    try {
      session = hsClient.openIndexSession(database, table, indexName, columns);
      sessionCache.put(key, session);
      return session;
    } catch (final Exception e) {
      if (e instanceof InterruptedException) Thread.currentThread().interrupt();
      throw new IllegalStateException("HandlerSocket can't open index.", e);
    }
  }

  private final HSClient hsClient;
  private final Dispatcher dispatcher = new Dispatcher();
  private final Map<Object, IndexSession> sessionCache;
  private final Map<Method, InvocationHandler> method2Handler;

  private final class Dispatcher implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      return method2Handler.get(method).invoke(proxy, method, args);
    }
  }
}
