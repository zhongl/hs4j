package com.github.zhongl.hs4j.kit;

import static com.github.zhongl.hs4j.kit.annotations.HandlerSocket.Command.*;
import static com.google.code.hs4j.FindOperator.*;

import java.sql.*;
import java.util.*;

import com.github.zhongl.hs4j.kit.annotations.*;

/**
 * {@link UserRepository}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-2
 * 
 */
@Repository(database = "test", table = "user_t")
@Entity(User.class)
interface UserRepository {

  @HandlerSocket(INSERT)
  void add(User user);

  @HandlerSocket(INSERT)
  void addUser(long id, String firsName, String lastName, int age) throws SQLException;

  @HandlerSocket(DELETE)
  void delete(User user);

  @HandlerSocket(DELETE)
  @Operator(LT)
  @Index("AGE")
  void deleteUserAgeLessThan(@Key int age);

  @HandlerSocket(FIND)
  @Operator(GT)
  @Index("AGE")
  Collection<User> findUserAgeGreaterThan(@Key int age, @Offset int offset, @Limit int limit);

  @HandlerSocket(FIND)
  User findUserById(@Key long value);

  @HandlerSocket(FIND)
  @Index("FULL_NAME")
  User findUserByFullName(@Key String firstName, @Key String lastName);

  @HandlerSocket(FIND)
  @Index("AGE")
  @Operator(GT)
  User findUserBy(@Key int age, @In("last_name") Set<String> nameSet);

  @HandlerSocket(UPDATE)
  void update(User user);

  @HandlerSocket(UPDATE)
  @Columns({ "name", "age" })
  @Index("NAME")
  void updateUserAge(@Key String name, int age);

}
