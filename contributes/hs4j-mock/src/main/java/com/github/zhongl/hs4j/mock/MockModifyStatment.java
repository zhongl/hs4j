package com.github.zhongl.hs4j.mock;

import static com.github.zhongl.hs4j.mock.HsqldbSession.*;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

import com.google.code.hs4j.*;
import com.google.code.hs4j.exception.*;

/**
 * {@link MockModifyStatment}
 * @author  <a href=mailto:jushi@taobao.com>jushi</a>
 * @created 2011-6-8
 * 
 */
final class MockModifyStatment implements ModifyStatement {
  private final HSClient hsClient;
  private final List<String> values;
  private final int indexId;

  public MockModifyStatment(HSClient hsClient, int indexId) {
    this.hsClient = hsClient;
    this.indexId = indexId;
    values = new ArrayList<String>();
  }

  @Override
  public int update(String[] keys, FindOperator operator) throws InterruptedException,
                                                         TimeoutException,
                                                         HandlerSocketException {
    return update(keys, operator, DEFAULT_LIMIT, DEFAULT_OFFSET);
  }

  @Override
  public int update(String[] keys, FindOperator operator, int limit, int offset) throws InterruptedException,
                                                                                TimeoutException,
                                                                                HandlerSocketException {
    return hsClient.update(indexId, keys, values(), operator, limit, offset);
  }

  private String[] values() {
    return values.toArray(new String[0]);
  }

  @Override
  public void setString(int parameterIndex, String x) {
    values.set(parameterIndex, x);
  }

  @Override
  public void setShort(int parameterIndex, short x) {
    values.set(parameterIndex, String.valueOf(x));
  }

  @Override
  public void setLong(int parameterIndex, long x) {
    values.set(parameterIndex, String.valueOf(x));
  }

  @Override
  public void setInt(int parameterIndex, int x) {
    values.set(parameterIndex, String.valueOf(x));
  }

  @Override
  public void setFloat(int parameterIndex, float x) {
    values.set(parameterIndex, String.valueOf(x));
  }

  @Override
  public void setDouble(int parameterIndex, double x) {
    values.set(parameterIndex, String.valueOf(x));
  }

  @Override
  public void setClob(int parameterIndex, Clob x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBytes(int parameterIndex, byte[] x) {
    values.set(parameterIndex, new String(x));
  }

  @Override
  public void setByte(int parameterIndex, byte x) {
    setBytes(parameterIndex, new byte[] { x });
  }

  @Override
  public void setBoolean(int parameterIndex, boolean x) {
    values.set(parameterIndex, String.valueOf(x));
  }

  @Override
  public void setBlob(int parameterIndex, Blob x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBigDecimal(int parameterIndex, BigDecimal x) {
    values.set(parameterIndex, x.toString());
  }

  @Override
  public boolean insert() throws HandlerSocketException, InterruptedException, TimeoutException {
    return hsClient.insert(indexId, values());
  }
}
