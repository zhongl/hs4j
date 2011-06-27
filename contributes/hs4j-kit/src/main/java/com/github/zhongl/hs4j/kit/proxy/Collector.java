package com.github.zhongl.hs4j.kit.proxy;

/**
 * {@link Collector}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-25
 * 
 */
public interface Collector<T> {
  T collectFrom(Object[] args);
}
