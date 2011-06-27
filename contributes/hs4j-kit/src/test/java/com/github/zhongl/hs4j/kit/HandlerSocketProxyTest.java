package com.github.zhongl.hs4j.kit;

import static com.github.zhongl.hs4j.kit.util.StringUtils.*;
import static com.google.code.hs4j.FindOperator.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.sql.*;

import org.junit.*;

import com.github.zhongl.hs4j.kit.proxy.*;
import com.google.code.hs4j.*;
import com.google.code.hs4j.exception.*;

/**
 * {@link HandlerSocketProxyTest}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-2
 * 
 */
public class HandlerSocketProxyTest {

  private static final int DEFAULT_LIMIT = Integer.MAX_VALUE;
  private static final int DEFAULT_OFFSET = 0;

  @Test
  public void addUser() throws Exception {
    final long id = 1L;
    final String firstName = "shi";
    final String lastName = "ju";
    final int age = 30;
    doReturnSessionWhenHsClientOpenIndexSession("PRIMARY", new String[] { "id", "first_name", "last_name", "age" });
    proxyFactory.newProxyOf(UserRepository.class).addUser(id, firstName, lastName, age);
    verify(session).insert(toStringArray(id, firstName, lastName, age));
  }

  @Test(expected = SQLException.class)
  public void castException() throws Exception {
    final long id = 1L;
    final String firstName = "shi";
    final String lastName = "ju";
    final int age = 30;
    doReturnSessionWhenHsClientOpenIndexSession("PRIMARY", new String[] { "id", "first_name", "last_name", "age" });
    doThrow(new HandlerSocketException()).when(session).insert((String[]) any());
    proxyFactory.newProxyOf(UserRepository.class).addUser(id, firstName, lastName, age);
  }

  @Test
  public void addUserObject() throws Exception {
    final long id = 1L;
    final String firstName = "shi";
    final String lastName = "ju";
    final int age = 30;
    doReturnSessionWhenHsClientOpenIndexSession("PRIMARY", new String[] { "id", "first_name", "last_name", "age" });
    proxyFactory.newProxyOf(UserRepository.class).add(new User(id, firstName, lastName, age));
    verify(session).insert(toStringArray(id, firstName, lastName, age));
  }

  @Test
  public void deleteUser() throws Exception {
    final long id = 1L;
    final String firstName = "shi";
    final String lastName = "ju";
    final int age = 22;
    doReturnSessionWhenHsClientOpenIndexSession("PRIMARY", new String[] { "id", "first_name", "last_name", "age" });
    proxyFactory.newProxyOf(UserRepository.class).delete(new User(id, firstName, lastName, age));
    verify(session).delete(toStringArray(id), EQ, DEFAULT_LIMIT, DEFAULT_OFFSET);
  }

  @Test
  public void deleteUserAgeLessThan() throws Exception {
    final int age = 22;
    doReturnSessionWhenHsClientOpenIndexSession("AGE", new String[] { "id", "first_name", "last_name", "age" });
    proxyFactory.newProxyOf(UserRepository.class).deleteUserAgeLessThan(age);
    verify(session).delete(toStringArray(age), LT, DEFAULT_LIMIT, DEFAULT_OFFSET);
  }

  @Test
  public void findUserAgeGreateThan() throws Exception {
    final int age = 30;
    final int offset = 0;
    final int limit = 100;
    ResultSet resultSet = mock(ResultSet.class);
    doReturn(resultSet).when(session).find(toStringArray(age), GT, limit, offset);
    doReturnSessionWhenHsClientOpenIndexSession("AGE", new String[] { "id", "first_name", "last_name", "age" });
    proxyFactory.newProxyOf(UserRepository.class).findUserAgeGreaterThan(age, offset, limit);
  }

  @Test
  public void findUserById() throws Exception {
    final long id = 1L;
    ResultSet resultSet = mock(ResultSet.class);
    doReturn(resultSet).when(session).find(toStringArray(id), EQ, 1, 0);
    doReturnSessionWhenHsClientOpenIndexSession("PRIMARY", new String[] { "id", "first_name", "last_name", "age" });
    proxyFactory.newProxyOf(UserRepository.class).findUserById(id);
  }

  @Before
  public void setUp() throws Exception {
    reset(hsClient, session);
  }

  @Test
  public void updateUserAge() throws Exception {
    final String name = "zhongl";
    final int age = 22;
    doReturnSessionWhenHsClientOpenIndexSession("NAME", new String[] { "name", "age" });
    proxyFactory.newProxyOf(UserRepository.class).updateUserAge(name, age);
    verify(session).update(toStringArray(name), toStringArray(age), EQ, DEFAULT_LIMIT, DEFAULT_OFFSET);
  }

  @Test
  public void updateUserObject() throws Exception {
    final long id = 1L;
    final String firstName = "shi";
    final String lastName = "ju";
    final int age = 22;
    doReturnSessionWhenHsClientOpenIndexSession("PRIMARY", new String[] { "id", "first_name", "last_name", "age" });
    proxyFactory.newProxyOf(UserRepository.class).update(new User(id, firstName, lastName, age));
    verify(session).update(toStringArray(id),
                           toStringArray(firstName, lastName, age),
                           EQ,
                           DEFAULT_LIMIT,
                           DEFAULT_OFFSET);
  }

  private void doReturnSessionWhenHsClientOpenIndexSession(String index, final String[] columns) throws Exception {
    doReturn(session).when(hsClient).openIndexSession("test", "user_t", index, columns);
  }

  private final HSClient hsClient = mock(HSClient.class);
  private final IndexSession session = mock(IndexSession.class);
  private final ProxyFactory proxyFactory = new HandlerSocketProxyFactory(hsClient);

}
