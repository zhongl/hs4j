package com.github.zhongl.hs4j.kit.proxy;

import static com.github.zhongl.hs4j.kit.util.AssertUtils.*;

import java.lang.reflect.*;

import com.github.zhongl.hs4j.kit.annotations.*;
import com.github.zhongl.hs4j.kit.arguments.*;
import com.google.code.hs4j.*;

/**
 * {@link MethodAnnotations}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-25
 * 
 */
class MethodAnnotations {

  public MethodAnnotations(Method method) {
    this.method = method;
    handlerSocket = method.getAnnotation(HandlerSocket.class);
    index = method.getAnnotation(Index.class);
    columns = method.getAnnotation(Columns.class);
  }

  private FindOperator operatorOf(Method method) {
    Operator annotation = method.getAnnotation(Operator.class);
    return isEmpty(annotation) ? FindOperator.EQ : annotation.value();
  }

  public InvocationHandler createInvocationHandlerWith(IndexSession session) {
    if (isEmpty(handlerSocket)) return new SelfInvocationHandler();
    FindOperator operator = operatorOf(method);
    ParameterAnnotations parameterAnnotations = new ParameterAnnotations(method);
    ReturnType returnType = new ReturnType(method);

    switch (handlerSocket.value()) {
      case INSERT:
        return new InsertHandler(session, parameterAnnotations, returnType);
      case FIND:
        return new FindHandler(session, operator, parameterAnnotations, returnType);
      case UPDATE:
        return new UpdateHandler(session, operator, parameterAnnotations, returnType);
      case DELETE:
        return new DeleteHandler(session, operator, parameterAnnotations, returnType);
      default:
        throw new IllegalStateException("Illegal HandlerSocket command :" + handlerSocket.value());
    }
  }

  public String[] getColumnsOrDefault(String[] value) {
    return (isEmpty(columns) || isEmpty(columns.value())) ? value : columns.value();
  }

  public String getIndexNameOrDefaultBy(String value) {
    return isEmpty(index) ? value : index.value();
  }

  private final Method method;
  private final HandlerSocket handlerSocket;
  private final Index index;
  private final Columns columns;

  private final static class SelfInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      return method.invoke(proxy, args);
    }
  }

}
