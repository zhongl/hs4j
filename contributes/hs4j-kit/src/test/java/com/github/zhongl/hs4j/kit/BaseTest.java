package com.github.zhongl.hs4j.kit;

import static org.mockito.Mockito.*;

import org.junit.*;

import com.github.zhongl.hs4j.kit.proxy.*;
import com.google.code.hs4j.*;

/**
 * {@link BaseTest}
 * 
 * @author <a href=mailto:jushi@taobao.com>jushi</a>
 * @created Jun 30, 2011
 * 
 */
public abstract class BaseTest {

  private static final String DATABASE = "database";
  protected static final int DEFAULT_LIMIT = Integer.MAX_VALUE;
  protected static final int DEFAULT_OFFSET = 0;
  protected static final String PRIMARY = "PRIMARY";

  @Before
  public void setUp() throws Exception {
    reset(hsClient, session);
  }

  protected void doReturnSessionWhenHsClientOpenIndexSession(String index, final String[] columns) throws Exception {
    doReturn(session).when(hsClient).openIndexSession(DATABASE, "user_t", index, columns);
  }

  private final HSClient hsClient = mock(HSClient.class);
  protected final IndexSession session = mock(IndexSession.class);
  protected final ProxyFactory proxyFactory = new HandlerSocketProxyFactory(hsClient, DATABASE);

}
