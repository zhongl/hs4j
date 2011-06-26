package com.github.zhongl.hs4j.kit.util;

import java.lang.reflect.*;

/**
 * {@link AssertUtils}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-25
 * 
 */
public final class AssertUtils {
  private AssertUtils() {}

  public static boolean isEmpty(Object value) {
    if (value == null) return true;
    if (value.getClass().isArray()) return Array.getLength(value) == 0;
    if (value instanceof String) return ((String) value).length() == 0;
    return false;
  }
  
  public static boolean isNotEmpty(Object value) {
    return !isEmpty(value);
  }
}
