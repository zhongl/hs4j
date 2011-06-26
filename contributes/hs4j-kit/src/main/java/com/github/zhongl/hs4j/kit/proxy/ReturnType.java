package com.github.zhongl.hs4j.kit.proxy;

import java.lang.reflect.*;
import java.sql.*;

/**
 * {@link ReturnType}
 * @author  <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-25
 * 
 */
class ReturnType {

  private final Method method;

  public ReturnType(Method method) {
    this.method = method;
  }

  public Object cast(int result) {
    // TODO Auto-generated method stub
    return null;
  }

  public Object cast(ResultSet result) {
    // TODO Auto-generated method stub
    return null;
  }

  public Object cast(boolean result) {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean isNot(Class<?> clazz) {
    return !is(clazz);
  }

  public boolean is(Class<?> clazz) {
    return method.getReturnType().equals(clazz);
  }

  /**
   * @return true if type is single object but not primitive type or it wrapper.
   */
  public boolean isSingleEntity() {
    // TODO Auto-generated method stub
    return false;
  }

}
