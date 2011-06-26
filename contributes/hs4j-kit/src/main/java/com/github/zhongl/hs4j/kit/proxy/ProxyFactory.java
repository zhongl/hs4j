package com.github.zhongl.hs4j.kit.proxy;

import java.lang.reflect.*;

/**
 * {@link ProxyFactory}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-2
 * 
 */
public abstract class ProxyFactory {

  private static <T> Class<?>[] interfaces(Class<T> clazz) {
    if (!clazz.isInterface()) throw new IllegalArgumentException(clazz + " should be a interface.");
    return new Class<?>[] { clazz };
  }

  @SuppressWarnings("unchecked")
  public <T> T newProxyOf(Class<T> clazz) {
    return (T) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces(clazz), handlerOf(clazz));
  }

  protected abstract InvocationHandler handlerOf(Class<?> clazz);
}
