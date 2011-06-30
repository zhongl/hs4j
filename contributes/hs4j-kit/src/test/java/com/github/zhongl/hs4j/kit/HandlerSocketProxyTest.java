package com.github.zhongl.hs4j.kit;

import static com.github.zhongl.hs4j.kit.util.StringUtils.*;
import static com.google.code.hs4j.FindOperator.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.*;

import org.junit.*;
import org.mockito.invocation.*;
import org.mockito.stubbing.*;

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

  private static final String DATABASE = "database";
  private final static class NextTrueTimeAnswer implements Answer<Boolean> {
    
    private int time;

    public NextTrueTimeAnswer(int time) {
      this.time = time;
    }

    @Override
    public Boolean answer(InvocationOnMock invocation) throws Throwable {
      return --time >= 0;
    }
  }
  
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
    doReturn(true).when(session).insert(toStringArray(id, firstName, lastName, age));
    boolean result = proxyFactory.newProxyOf(UserRepository.class).add(new User(id, firstName, lastName, age));
    assertThat(result, is(true));
  }

  @Test
  public void deleteUser() throws Exception {
    final long id = 1L;
    final String firstName = "shi";
    final String lastName = "ju";
    final int age = 22;
    doReturnSessionWhenHsClientOpenIndexSession("PRIMARY", new String[] { "id", "first_name", "last_name", "age" });
    doReturn(1).when(session).delete(toStringArray(id), EQ, DEFAULT_LIMIT, DEFAULT_OFFSET);
    int deleted = proxyFactory.newProxyOf(UserRepository.class).delete(new User(id, firstName, lastName, age));
    assertThat(deleted, is(1));
  }
  
  @Test
  public void findUserByFullName() throws Exception {
    String firstName = "shi";
    String lastName = "ju";
    ResultSet resultSet = mock(ResultSet.class);
    doAnswer(new NextTrueTimeAnswer(1)).when(resultSet).next();
    doReturn(resultSet).when(session).find(toStringArray(firstName,lastName), EQ, 1, 0);
    doReturnSessionWhenHsClientOpenIndexSession("FULL_NAME", new String[] { "id", "first_name", "last_name", "age" });
    User user = proxyFactory.newProxyOf(UserRepository.class).findUserByFullName(firstName, lastName);
    assertThat(user, is(notNullValue()));
  }
  
  @Test
  public void findLastNameOfUserByAge() throws Exception {
    int age = 22;
    ResultSet resultSet = mock(ResultSet.class);
    doAnswer(new NextTrueTimeAnswer(3)).when(resultSet).next();
    doReturn("name").when(resultSet).getString(0);
    doReturn(resultSet).when(session).find(toStringArray(age), EQ, DEFAULT_LIMIT, DEFAULT_OFFSET);
    doReturnSessionWhenHsClientOpenIndexSession("AGE", new String[] { "last_name", "age" });
    Collection<String> names = proxyFactory.newProxyOf(UserRepository.class).findLastNameOfUserByAge(age );
    assertThat(names.size(), is(3));
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
    doAnswer(new NextTrueTimeAnswer(3)).when(resultSet).next();
    doReturn(resultSet).when(session).find(toStringArray(age), GT, limit, offset);
    doReturnSessionWhenHsClientOpenIndexSession("AGE", new String[] { "id", "first_name", "last_name", "age" });
    Collection<User> users = proxyFactory.newProxyOf(UserRepository.class).findUserAgeGreaterThan(age, offset, limit);
    assertThat(users.size(), is(3));
  }

  @Test
  public void findUserById() throws Exception {
    final long id = 1L;
    ResultSet resultSet = mock(ResultSet.class);
    doAnswer(new NextTrueTimeAnswer(1)).when(resultSet).next();
    doReturn(resultSet).when(session).find(toStringArray(id), EQ, 1, 0);
    doReturnSessionWhenHsClientOpenIndexSession("PRIMARY", new String[] { "id", "first_name", "last_name", "age" });
    User user = proxyFactory.newProxyOf(UserRepository.class).findUserById(id);
    assertThat(user, is(notNullValue()));
  }

  @Before
  public void setUp() throws Exception {
    reset(hsClient, session);
  }

  @Test
  public void updateUserAge() throws Exception {
    final String name = "zhongl";
    final int age = 22;
    doReturnSessionWhenHsClientOpenIndexSession("NAME", new String[] { "last_name", "age" });
    doReturn(3).when(session).update(toStringArray(name), toStringArray(age), EQ, DEFAULT_LIMIT, DEFAULT_OFFSET);
    int changed = proxyFactory.newProxyOf(UserRepository.class).updateUserAge(name, age);
    assertThat(changed, is(3));
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
    doReturn(session).when(hsClient).openIndexSession(DATABASE, "user_t", index, columns);
  }

  private final HSClient hsClient = mock(HSClient.class);
  private final IndexSession session = mock(IndexSession.class);
  private final ProxyFactory proxyFactory = new HandlerSocketProxyFactory(hsClient, DATABASE);

}
