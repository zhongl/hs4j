package com.github.zhongl.hs4j.mock;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;

import com.google.code.hs4j.*;

/**
 * {@link MockHsClientTest}
 * 
 * @author <a href=mailto:jushi@taobao.com>jushi</a>
 * @created 2011-6-8
 * 
 */
public class MockHsClientTest {
  private HSClient hsClient;

  @Before
  public void setUp() throws Exception {
    String ddls = "CREATE TABLE user_t (id INTEGER , name VARCHAR(32), age INTEGER)";
    hsClient = new MockHsClient(ddls);
  }

  @After
  public void tearDown() throws Exception {
    hsClient.shutdown();
  }

  @Test
  public void indexSession() throws Exception {
    String[] columns = { "id", "name", "age" };
    String[] values = { "1", "jushi", "18" };
    String[] keys = new String[] { "1" };
    FindOperator operator = FindOperator.EQ;
    IndexSession session = hsClient.openIndexSession(null, "user_t", "id", columns);
    session.insert(values);
    ResultSet resultSet = session.find(keys);
    assertThat(resultSet.next(), is(true));
    assertThat(resultSet.getInt(1), is(1));
    assertThat(resultSet.getString(2), is("jushi"));
    assertThat(resultSet.getInt(3), is(18));

    String[] newValues = { "1", "jushi", "28" };
    session.update(keys, newValues, operator);
    resultSet = session.find(keys);
    assertThat(resultSet.next(), is(true));
    assertThat(resultSet.getInt(1), is(1));
    assertThat(resultSet.getString(2), is("jushi"));
    assertThat(resultSet.getInt(3), is(28));

    session.delete(keys);
    resultSet = session.find(keys);
    assertThat(resultSet.next(), is(false));
  }
}
