package com.github.zhongl.hs4j.kit.util;

import java.util.*;

/**
 * {@link CollectionUtils}
 * 
 * @author <a href=mailto:jushi@taobao.com>jushi</a>
 * @created Jun 27, 2011
 * 
 */
public class CollectionUtils {
  public static <T> Set<T> set(T... values) {
    Set<T> set = new HashSet<T>(values.length);
    for (T value : values)
      set.add(value);
    return set;
  }

  private CollectionUtils() {}

}
