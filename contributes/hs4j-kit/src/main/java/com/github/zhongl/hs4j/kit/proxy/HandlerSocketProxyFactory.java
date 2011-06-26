package com.github.zhongl.hs4j.kit.proxy;

import static com.github.zhongl.hs4j.kit.util.StringUtils.*;
import static org.apache.commons.codec.digest.DigestUtils.*;

import java.lang.reflect.*;
import java.util.*;

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
      String indexName = methodAnnotations.getIndexNameOrDefaultBy(classAnnotations.getIndexName());
      String[] columns = methodAnnotations.getColumnsOrDefault(classAnnotations.getColumnsOrThrowExceptionWith(method));
      IndexSession indexSession = getOrCreateIndexSessionBy(database, table, indexName, columns);
      method2Handler.put(method, methodAnnotations.createInvocationHandlerWith(indexSession));
    }
    return dispatcher;
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
