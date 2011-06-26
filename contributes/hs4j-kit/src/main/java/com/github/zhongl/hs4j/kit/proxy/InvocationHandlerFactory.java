package com.github.zhongl.hs4j.kit.proxy;

import java.lang.reflect.*;

import com.github.zhongl.hs4j.kit.arguments.*;
import com.google.code.hs4j.*;

/**
 * {@link InvocationHandlerFactory}
 * @author  <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-26
 * 
 */
public interface InvocationHandlerFactory {

  InvocationHandler createWith(IndexSession indexSession,
                               ParameterAnnotations parameterAnnotations,
                               FindOperator findOperator,
                               ReturnType returnType);
}