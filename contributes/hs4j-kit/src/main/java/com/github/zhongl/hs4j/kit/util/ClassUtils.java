package com.github.zhongl.hs4j.kit.util;

import java.lang.reflect.*;

/**
 * {@link ClassUtils}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-5
 * 
 */
public class ClassUtils {
  public static void assertMethodReturnType(Method method, Class<?> returnType) {
    if (method.getReturnType().equals(returnType)) return;
    throw new IllegalArgumentException(returnType + " should return by method: " + method);
  }

  @SuppressWarnings("unchecked")
  public static <T> Class<T> getGenericClassFrom(Type genericReturnType) throws ClassNotFoundException {
    final String sigin = genericReturnType.toString();
    final int begin = sigin.indexOf('<') + 1;
    final int end = sigin.indexOf('>');
    return (Class<T>) Class.forName(sigin.substring(begin, end));
  }

  private ClassUtils() {}
}
