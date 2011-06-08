package com.github.zhongl.hs4j.mock;

import static java.text.MessageFormat.*;

import java.sql.*;

import com.google.code.hs4j.*;

/**
 * {@link HsqldbSession}
 * 
 * @author <a href=mailto:jushi@taobao.com>jushi</a>
 * @created 2011-6-8
 * 
 */
public class HsqldbSession {

  private static String commaJoin(String[] strings) {
    StringBuilder sb = new StringBuilder();
    for (String string : strings) {
      sb.append(",").append(string);
    }
    return sb.toString().substring(1);
  }

  public int delete(String[] keys, FindOperator operator, int limit, int offset) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      return statement.executeUpdate(deleteTableSqlWith(operator.getValue(), keys[0]));
    } finally {
      statement.close();
    }
  }

  public boolean insert(String[] values) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      return statement.execute(insertTableSqlWith(quoteString(values)));
    } finally {
      statement.close();
    }
  }

  private String[] quoteString(String[] values) {
    for (int i = 0; i < values.length; i++) {
      String value = values[i];
      try {
        Long.parseLong(value);
      } catch (NumberFormatException e) {
        try {
          Double.parseDouble(value);
        } catch (NumberFormatException e1) {
          values[i] = "'" + value + "'";
        }
      }
    }
    return values;
  }

  public int update(String[] keys, String[] values, FindOperator operator, int limit, int offset) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      return statement.executeUpdate(updateTableSqlWith(quoteString(values), operator.getValue(), keys[0]));
    } finally {
      statement.close();
    }
  }

  private String deleteTableSqlWith(String operator, String key) {
    return format("DELETE FROM {0} WHERE {1}{2}{3}", table, index, operator, key);
  }

  private String emptyOrSet(String token, int value) {
    return value > 0 ? " " + token + " " + value : "";
  }

  private String insertTableSqlWith(String[] values) {
    return format("INSERT INTO {0}({1}) VALUES({2})", table, commaJoin(columns), commaJoin(values));
  }

  private String[] mapColumnsTo(String[] values) {
    String[] pairs = new String[values.length];
    for (int i = 0; i < pairs.length; i++) {
      pairs[i] = columns[i] + "=" + values[i];
    }
    return pairs;
  }

  private String selectTableSqlWith(String operator, String key, int limit, int offset) {
    return format("SELECT {0} FROM {1} WHERE {2}{3}{4}",
                  commaJoin(columns),
                  table,
                  index,
                  operator,
                  key,
                  emptyOrSet("LIMIT", limit),
                  emptyOrSet("OFFSET", offset));
  }

  private String updateTableSqlWith(String[] values, String operator, String key) {
    return format("UPDATE {0} SET {1} WHERE {2}{3}{4}", table, commaJoin(mapColumnsTo(values)), index, operator, key);
  }

  private final Connection connection;

  private final String table;

  private final String index;

  private final String[] columns;

  static final int DEFAULT_LIMIT = -1;

  static final int DEFAULT_OFFSET = -1;

  public HsqldbSession(Connection connection, String table, String index, String[] columns) throws SQLException {
    this.connection = connection;
    this.table = table;
    this.index = index;
    this.columns = columns;
  }

  public ResultSet find(String[] keys, FindOperator operator, int limit, int offset) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      return statement.executeQuery(selectTableSqlWith(operator.getValue(), keys[0], limit, offset));
    } finally {
      statement.close();
    }
  }

}
