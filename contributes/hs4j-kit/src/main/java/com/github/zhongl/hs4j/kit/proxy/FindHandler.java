package com.github.zhongl.hs4j.kit.proxy;

import java.lang.reflect.*;

import com.github.zhongl.hs4j.kit.arguments.*;
import com.google.code.hs4j.*;

/**
 * {@link FindHandler}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-26
 * 
 */
public class FindHandler implements InvocationHandler {

  public FindHandler(IndexSession session,
                     FindOperator operator,
                     ParameterAnnotations parameterAnnotations,
                     ReturnType returnType) {
    assertVaild(returnType);
    this.session = session;
    this.operator = operator;
    this.returnType = returnType;
    keysCollector = parameterAnnotations.apply(new KeysCollector());
    limit  = new Limit(parameterAnnotations, returnType);
  }
  
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    return returnType.cast(session.find(keys(args), operator, limit(args), offset(args)));
  }

  private void assertVaild(ReturnType returnType) {
    if (returnType.is(void.class))
      throw new IllegalArgumentException(returnType + " is unsupported with FIND command.");
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
  private final ReturnType returnType;
  private final FindOperator operator;
  private final Collector<String[]> keysCollector;
  private final Limit limit;

}
