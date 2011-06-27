package com.github.zhongl.hs4j.kit.proxy;

import java.sql.*;
import java.util.*;

/**
 * {@link ResultSetGetters}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-3
 * 
 */
public class ResultSetGetters {

  private static final Map<Class<?>, ResultSetGetter<?>> map;

  static {
    map = new HashMap<Class<?>, ResultSetGetter<?>>();
    map.put(Boolean.TYPE, new BooleanResultSetGetter());
    map.put(Boolean.class, new BooleanResultSetGetter());
    map.put(Short.TYPE, new ShortResultSetGetter());
    map.put(Short.class, new ShortResultSetGetter());
    map.put(Integer.TYPE, new IntegerResultSetGetter());
    map.put(Integer.class, new IntegerResultSetGetter());
    map.put(Long.TYPE, new LongResultSetGetter());
    map.put(Long.class, new LongResultSetGetter());
    map.put(String.class, new StringResultSetGetter());
  }

  @SuppressWarnings("unchecked")
  public static <T> ResultSetGetter<T> resultSetGetterOf(Class<T> clazz) {
    final ResultSetGetter<T> resultSetGetter = (ResultSetGetter<T>) map.get(clazz);
    if (resultSetGetter == null) throw new IllegalArgumentException("Unsupported class: " + clazz);
    return resultSetGetter;
  }

  public interface ResultSetGetter<T> {
    public abstract T get(ResultSet resultSet, String columnLabel) throws SQLException;
  }

  private static class BooleanResultSetGetter implements ResultSetGetter<Boolean> {
    @Override
    public Boolean get(ResultSet resultSet, String columnLabel) throws SQLException {
      return resultSet.getBoolean(columnLabel);
    }
  }

  private static class IntegerResultSetGetter implements ResultSetGetter<Integer> {
    @Override
    public Integer get(ResultSet resultSet, String columnLabel) throws SQLException {
      return resultSet.getInt(columnLabel);
    }
  }

  private static class LongResultSetGetter implements ResultSetGetter<Long> {
    @Override
    public Long get(ResultSet resultSet, String columnLabel) throws SQLException {
      return resultSet.getLong(columnLabel);
    }
  }

  private static class ShortResultSetGetter implements ResultSetGetter<Short> {
    @Override
    public Short get(ResultSet resultSet, String columnLabel) throws SQLException {
      return resultSet.getShort(columnLabel);
    }
  }

  private static class StringResultSetGetter implements ResultSetGetter<String> {
    @Override
    public String get(ResultSet resultSet, String columnLabel) throws SQLException {
      return resultSet.getString(columnLabel);
    }
  }

}
