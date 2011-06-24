package com.github.zhongl.hs4j.kit;

import static com.google.code.hs4j.FindOperator.*;

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

  @Insert
  void add(User user) throws Exception;

  @Insert
  @Columns({ "seq", "name", "age" })
  void addUser(long id, String name, int age);

  @Delete
  void delete(User user);

  @Delete(LT)
  @Index("AGE")
  void deleteUserAgeLessThan(int age);

  @Find(GT)
  @Index("AGE")
  Collection<User> findUserAgeGreaterThan(int age, @Offset int offset, @Limit int limit);

  @Find
  User findUserById(long value);

  @Update
  void update(User user);

  @Update
  @Columns("age")
  @Index("NAME")
  void updateUserAge(String name, int age);

}
