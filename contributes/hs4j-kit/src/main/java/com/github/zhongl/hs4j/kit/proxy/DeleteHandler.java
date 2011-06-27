package com.github.zhongl.hs4j.kit.proxy;

import java.lang.reflect.*;

import com.github.zhongl.hs4j.kit.proxy.EntityCollector.CollectStrategy;
import com.google.code.hs4j.*;

/**
 * {@link DeleteHandler}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-26
 * 
 */
public class DeleteHandler implements InvocationHandler {

  public DeleteHandler(IndexSession session,
                       FindOperator operator,
                       ParameterAnnotations parameterAnnotations,
                       ReturnType returnType) {
    this.session = session;
    this.operator = operator;
    this.returnType = returnType;
    limit = new LimitPair(parameterAnnotations, returnType);
    keysCollector =
      parameterAnnotations.isEntityTypeParameter() ? new EntityCollector(CollectStrategy.KEYS)
        : parameterAnnotations.apply(new KeysCollector());
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    return returnType.cast(session.delete(keys(args), operator, limit(args), offset(args)));
  }

  private String[] keys(Object[] args) {
    return keysCollector.collectFrom(args);
  }

  private int limit(Object[] args) {
    return limit.limitCollector.collectFrom(args);
  }

  private int offset(Object[] args) {
    return limit.offsetCollector.collectFrom(args);
  }

  private final IndexSession session;
  private final FindOperator operator;
  private final ReturnType returnType;
  private final Collector<String[]> keysCollector;
  private final LimitPair limit;

}
