package com.github.zhongl.hs4j.kit.util;

import java.lang.reflect.*;
import java.util.*;

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
  public static <T> Class<T> getOnlyOneTypeArgumentClassFrom(ParameterizedType parameterizedType) {
    final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
    if (actualTypeArguments.length > 1)
      throw new IllegalArgumentException(parameterizedType + " has more than one type arguments");
    return (Class<T>) actualTypeArguments[0];
  }

  public static boolean isBoolean(Class<?> type) {
    return Boolean.class.equals(type) || Boolean.TYPE.equals(type);
  }

  public static boolean isByte(Class<?> type) {
    return Byte.class.equals(type) || Byte.TYPE.equals(type);
  }

  public static boolean isCharacter(Class<?> type) {
    return Character.class.equals(type) || Character.TYPE.equals(type);
  }

  public static boolean isCollection(Class<?> type) {
    return Collection.class.isAssignableFrom(type);
  }

  public static boolean isDouble(Class<?> type) {
    return Double.class.equals(type) || Double.TYPE.equals(type);
  }

  public static boolean isFloat(Class<?> type) {
    return Float.class.equals(type) || Float.TYPE.equals(type);
  }

  public static boolean isInteger(Class<?> type) {
    return Integer.class.equals(type) || Integer.TYPE.equals(type);
  }

  public static boolean isIterable(Class<?> rawType) {
    return Iterable.class.isAssignableFrom(rawType);
  }

  public static boolean isIterator(Class<?> type) {
    return Iterator.class.isAssignableFrom(type);
  }

  public static boolean isLong(Class<?> type) {
    return Long.class.equals(type) || Long.TYPE.equals(type);
  }

  public static boolean isParameterizedType(Type type) {
    return type instanceof ParameterizedType;
  }

  public static boolean isShort(Class<?> type) {
    return Short.class.equals(type) || Short.TYPE.equals(type);
  }

  public static boolean isString(Class<?> type) {
    return String.class.equals(type);
  }

  public static ParameterizedType parameterized(Type type) {
    return (ParameterizedType) type;
  }

  private ClassUtils() {}

}
