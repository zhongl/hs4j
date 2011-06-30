package com.github.zhongl.hs4j.kit;

import static com.github.zhongl.hs4j.kit.annotations.HandlerSocket.Command.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hamcrest.*;
import org.junit.*;

import com.github.zhongl.hs4j.kit.annotations.*;

/**
 * {@link InvalidAnnotationUsingTest}
 * 
 * @author <a href=mailto:jushi@taobao.com>jushi</a>
 * @created Jun 30, 2011
 * 
 */
public class InvalidAnnotationUsingTest extends BaseTest {

  @Test
  public void invalidReturnTypeForInsert() throws Exception {
    try {
      proxyFactory.newProxyOf(InvalidReturnTypeForInsert.class);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is(matche(".+type=int.+INSERT.+")));
    }
  }

  @Test
  public void noColumns() throws Exception {
    try {
      proxyFactory.newProxyOf(NoColumns.class);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is(matche(".+NoColumns\\.method\\(int\\).+@Columns")));
    }
  }

  @Test
  public void noKey() throws Exception {
    try {
      proxyFactory.newProxyOf(NoKey.class);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is(matche(".+@Key -> .+deleteById.+")));
    }
  }

  @Test
  public void noTable() throws Exception {
    try {
      proxyFactory.newProxyOf(NoTable.class);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is(matche(".+NoTable.+@Table")));
    }
  }

  @Test
  public void noValues() throws Exception {
    try {
      proxyFactory.newProxyOf(NoValues.class);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is(matche(".+as value -> .+NoValues\\.add\\(int\\)")));
    }
  }

  @Override
  @Before
  public void setUp() throws Exception {
    doReturnSessionWhenHsClientOpenIndexSession(PRIMARY, new String[] { "field" });
  }

  @Test
  public void VoidReturnTypeForFind() throws Exception {
    try {
      proxyFactory.newProxyOf(VoidReturnTypeForFind.class);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is(matche(".+type=void.+FIND.+")));
    }
  }

  private StringMatcher matche(String regex) {
    return new StringMatcher(regex);
  }

  static class EntityClass {
    public final int field = 1;
  }

  @Table("table")
  @Entity(EntityClass.class)
  interface InvalidReturnTypeForInsert {
    @HandlerSocket(INSERT)
    public int add(EntityClass entityClass);

  }

  @Table("table")
  interface NoColumns {
    @HandlerSocket(DELETE)
    public void method(int value);
  }

  @Table("table")
  @Entity(EntityClass.class)
  interface NoKey {
    @HandlerSocket(DELETE)
    public void deleteById(int value);
  }

  interface NoTable {
    public void method(int value);
  }

  @Table("table")
  @Entity(EntityClass.class)
  interface NoValues {
    @HandlerSocket(INSERT)
    public void add(@Key int value);
  }

  @Table("table")
  @Entity(EntityClass.class)
  interface VoidReturnTypeForFind {
    @HandlerSocket(FIND)
    public void findById(int value);
  }

  private final class StringMatcher extends BaseMatcher<String> {
    public StringMatcher(String regex) {
      this.regex = regex;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("match regex ");
      description.appendValue(regex);

    }

    @Override
    public boolean matches(Object item) {
      return ((String) item).matches(regex);
    }

    private final String regex;
  }

}
