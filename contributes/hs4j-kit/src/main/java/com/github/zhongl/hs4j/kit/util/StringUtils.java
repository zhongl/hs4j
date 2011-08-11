package com.github.zhongl.hs4j.kit.util;

import java.util.*;

/**
 * {@link StringUtils}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-3
 * 
 */
public class StringUtils {
  public static String join(String... strings) {
    final StringBuilder sb = new StringBuilder();
    for (final String string : strings)
      sb.append(string);
    return sb.toString();
  }

  public static String[] toStringArray(List<Object> objects) {
    String[] values = new String[objects.size()];
    for (int i = 0; i < values.length; i++) {
      values[i] = string(objects.get(i));
    }
    return values;
  }

  public static String[] toStringArray(Object... objects) {
    String[] values = new String[objects.length];
    for (int i = 0; i < values.length; i++) {
      values[i] = string(objects[i]);
    }
    return values;
  }

  public static String[] toStringArray(Object[] objects, int from, int to) {
    String[] values = new String[to - from];
    for (int i = 0; i < values.length; i++) {
      values[i] = string(objects[i + from]);
    }
    return values;
  }

  private static String string(Object object) {
    return object == null ? null : object.toString();
  }
}
