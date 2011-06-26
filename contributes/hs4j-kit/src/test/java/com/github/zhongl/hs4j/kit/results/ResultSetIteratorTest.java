package com.github.zhongl.hs4j.kit.results;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.*;

import org.junit.*;
import org.mockito.invocation.*;
import org.mockito.stubbing.*;

import com.github.zhongl.hs4j.kit.*;

/**
 * {@link ResultSetIteratorTest}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-5
 * 
 */
public class ResultSetIteratorTest {
  @Test
  public void user() throws Exception {
    final long id = 1L;
    final String firstName = "shi";
    final String lastName = "ju";
    final int age = 23;
    doAnswer(new NextAnswer(1)).when(resultSet).next();
    doReturn(id).when(resultSet).getLong("id");
    doReturn(firstName).when(resultSet).getString("first_name");
    doReturn(lastName).when(resultSet).getString("last_name");
    doReturn(age).when(resultSet).getInt("age");

    final ResultSetIterator iterator = new ResultSetIterator(User.class.getName(), resultSet);
    assertThat(iterator.hasNext(), is(true));
    final User user = (User) iterator.next();
    assertThat(user.id, is(id));
    assertThat(user.firstName, is(firstName));
    assertThat(user.lastName, is(lastName));
    assertThat(user.age, is(age));
  }

  private final ResultSet resultSet = mock(ResultSet.class);

  private final class NextAnswer implements Answer<Boolean> {
    public NextAnswer(int size) {
      this.size = size;
    }

    @Override
    public Boolean answer(InvocationOnMock invocation) throws Throwable {
      return size-- >= 0;
    }

    private int size;
  }
}
