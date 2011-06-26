package com.github.zhongl.hs4j.kit.proxy;

import static com.github.zhongl.hs4j.kit.arguments.EntityCollector.CollectStrategy.*;

import java.lang.reflect.*;

import com.github.zhongl.hs4j.kit.arguments.*;
import com.google.code.hs4j.*;

/**
 * {@link InsertHandler}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-26
 * 
 */
public class InsertHandler implements InvocationHandler {

  public InsertHandler(IndexSession session, ParameterAnnotations parameterAnnotations, ReturnType returnType) {
    assertValid(returnType);
    this.session = session;
    this.returnType = returnType;
    valuesCollector =
      parameterAnnotations.isEntityTypeParameter() ? new EntityCollector(ALL)
        : parameterAnnotations.apply(new ValuesCollector());
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    return returnType.cast(session.insert(valuesCollector.collectFrom(args)));
  }

  private void assertValid(final ReturnType returnType) {
    if (returnType.isNot(void.class) && returnType.isNot(boolean.class) && returnType.isNot(Boolean.class))
      throw new IllegalArgumentException(returnType + " is unsupported with INSERT command.");
  }

  private final IndexSession session;
  private final ReturnType returnType;
  private final Collector<String[]> valuesCollector;

}
