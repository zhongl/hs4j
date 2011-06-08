package com.github.zhongl.hs4j.mock;

import static com.github.zhongl.hs4j.mock.HsqldbSession.*;
import static com.google.code.hs4j.FindOperator.*;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import com.google.code.hs4j.*;
import com.google.code.hs4j.exception.*;
import com.google.code.hs4j.impl.*;

/**
 * {@link MockHsClient}
 * 
 * @author <a href=mailto:jushi@taobao.com>jushi</a>
 * @created 2011-6-8
 * 
 */
public class MockHsClient implements HSClient {

  public MockHsClient(String... ddls) {
    try {
      connection = getHsqldbConnection();
      createTablesBy(ddls);
      started = true;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ModifyStatement createStatement(int indexId) throws HandlerSocketException {
    return new MockModifyStatment(this, indexId);
  }

  @Override
  public int delete(int indexId, String[] keys) throws InterruptedException, TimeoutException, HandlerSocketException {
    return delete(indexId, keys, EQ);
  }

  @Override
  public int delete(int indexId, String[] keys, FindOperator operator) throws InterruptedException,
                                                                      TimeoutException,
                                                                      HandlerSocketException {
    return delete(indexId, keys, operator, HsqldbSession.DEFAULT_LIMIT, HsqldbSession.DEFAULT_OFFSET);
  }

  @Override
  public int delete(int indexId, String[] keys, FindOperator operator, int limit, int offset) throws InterruptedException,
                                                                                             TimeoutException,
                                                                                             HandlerSocketException {
    try {
      return map.get(indexId).delete(keys, operator, limit, offset);
    } catch (SQLException e) {
      throw new HandlerSocketException(e);
    }
  }

  @Override
  public ResultSet find(int indexId, String[] keys) throws InterruptedException,
                                                   TimeoutException,
                                                   HandlerSocketException {
    return find(indexId, keys, EQ, DEFAULT_LIMIT, DEFAULT_OFFSET);
  }

  @Override
  public ResultSet find(int indexId, String[] keys, FindOperator operator, int limit, int offset) throws InterruptedException,
                                                                                                 TimeoutException,
                                                                                                 HandlerSocketException {
    try {
      return map.get(indexId).find(keys, operator, limit, offset);
    } catch (SQLException e) {
      throw new HandlerSocketException(e);
    }
  }

  @Override
  public String getEncoding() {
    return null;
  }

  @Override
  public long getHealConnectionInterval() {
    return 0;
  }

  @Override
  public boolean insert(int indexId, String[] values) throws InterruptedException,
                                                     TimeoutException,
                                                     HandlerSocketException {
    try {
      return map.get(indexId).insert(values);
    } catch (SQLException e) {
      throw new HandlerSocketException(e);
    }
  }

  @Override
  public boolean isAllowAutoReconnect() {
    return false;
  }

  @Override
  public synchronized boolean isStarted() {
    return started;
  }

  @Override
  public boolean openIndex(int indexId, String dbname, String tableName, String indexName, String[] columns) throws InterruptedException,
                                                                                                            TimeoutException,
                                                                                                            HandlerSocketException {
    try {
      map.put(indexId, new HsqldbSession(connection, tableName, indexName, columns));
      return true;
    } catch (SQLException e) {
      throw new HandlerSocketException(e);
    }
  }

  @Override
  public IndexSession openIndexSession(int indexId, String dbname, String tableName, String indexName, String[] columns) throws InterruptedException,
                                                                                                                        TimeoutException,

                                                                                                                        HandlerSocketException {
    openIndex(indexId, dbname, tableName, indexName, columns);
    return new IndexSessionImpl(this, indexId, columns);
  }

  @Override
  public IndexSession openIndexSession(String dbname, String tableName, String indexName, String[] columns) throws InterruptedException,
                                                                                                           TimeoutException,
                                                                                                           HandlerSocketException {
    return openIndexSession(indexId.getAndIncrement(), dbname, tableName, indexName, columns);
  }

  @Override
  public void setAllowAutoReconnect(boolean allowAutoReconnect) {}

  @Override
  public void setEncoding(String encoding) {}

  @Override
  public void setHealConnectionInterval(long interval) {}

  @Override
  public void setOpTimeout(long opTimeout) {}

  @Override
  public synchronized void shutdown() throws IOException {
    if (!started) return;
    try {
      Statement statement = connection.createStatement();
      statement.execute("SHUTDOWN");
      statement.close();
    } catch (SQLException e) {}
    try {
      connection.close();
    } catch (SQLException e) {}
    started = false;
  }

  @Override
  public int update(int indexId, String[] keys, String[] values, FindOperator operator) throws InterruptedException,
                                                                                       TimeoutException,
                                                                                       HandlerSocketException {
    return update(indexId, keys, values, operator, DEFAULT_LIMIT, DEFAULT_OFFSET);
  }

  @Override
  public int update(int indexId, String[] keys, String[] values, FindOperator operator, int limit, int offset) throws InterruptedException,
                                                                                                              TimeoutException,
                                                                                                              HandlerSocketException {
    try {
      return map.get(indexId).update(keys, values, operator, limit, offset);
    } catch (SQLException e) {
      throw new HandlerSocketException(e);
    }
  }

  private void createTablesBy(String... ddls) throws SQLException {
    final Statement statement = connection.createStatement();
    try {
      for (String ddl : ddls)
        statement.execute(ddl);
    } catch (SQLException e) {
      connection.close();
      throw e;
    } finally {
      statement.close();
    }
  }

  private Connection getHsqldbConnection() throws ClassNotFoundException, SQLException {
    Class.forName("org.hsqldb.jdbcDriver");
    return DriverManager.getConnection("jdbc:hsqldb:mem:memdb", "SA", "");
  }

  private final Connection connection;

  private boolean started;

  private final AtomicInteger indexId = new AtomicInteger();

  private final Map<Integer, HsqldbSession> map = new HashMap<Integer, HsqldbSession>();

}
