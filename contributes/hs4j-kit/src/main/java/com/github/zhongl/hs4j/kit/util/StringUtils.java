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
  public static String[] toStringArray(Object... objects) {
    String[] values = new String[objects.length];
    for (int i = 0; i < values.length; i++) {
      values[i] = objects[i].toString();
    }
    return values;
  }

  public static String[] toStringArray(List<Object> objects) {
    String[] values = new String[objects.size()];
    for (int i = 0; i < values.length; i++) {
      values[i] = objects.get(i).toString();
    }
    return values;
  }

  public static String[] toStringArray(Object[] objects, int from, int to) {
    String[] values = new String[to - from];
    for (int i = 0; i < values.length; i++) {
      values[i] = objects[i + from].toString();
    }
    return values;
  }

  public static String join(String... strings) {
    final StringBuilder sb = new StringBuilder();
    for (final String string : strings)
      sb.append(string);
    return sb.toString();
  }
}
