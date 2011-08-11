package com.github.zhongl.hs4j.kit.proxy;

import static com.github.zhongl.hs4j.kit.proxy.EntityCollector.CollectStrategy.*;

import java.lang.reflect.*;

import com.google.code.hs4j.*;

/**
 * {@link UpdateHandler}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-26
 * 
 */
public class UpdateHandler implements InvocationHandler {

  public UpdateHandler(IndexSession session,
                       FindOperator operator,
                       ParameterAnnotations parameterAnnotations,
                       ReturnType returnType) {
    this.session = session;
    this.operator = operator;
    this.returnType = returnType;
    limit = new LimitPair(parameterAnnotations, returnType);
    if (parameterAnnotations.isEntityTypeParameter()) {
      keysCollector = new EntityCollector(KEYS);
      valuesCollector = new EntityCollector(ALL);
    } else {
      keysCollector = parameterAnnotations.apply(new KeysCollector());
      valuesCollector = parameterAnnotations.apply(new ValuesCollector());
    }
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    return returnType.cast(session.update(keys(args), values(args), operator, limit(args), offset(args)));
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

  private String[] values(Object[] args) {
    return valuesCollector.collectFrom(args);
  }

  private final IndexSession session;
  private final FindOperator operator;
  private final ReturnType returnType;
  private final LimitPair limit;
  private final Collector<String[]> valuesCollector;
  private final Collector<String[]> keysCollector;

}
