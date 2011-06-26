package com.github.zhongl.hs4j.kit;

import com.github.zhongl.hs4j.kit.annotations.*;
import com.github.zhongl.hs4j.kit.results.*;

public class User {

  public User(Long id, String firstName, String lastName, int age) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
  }

  public final Long id;

  @Column("first_name")
  public final String firstName;

  @Column("last_name")
  public final String lastName;

  public final int age;

}
