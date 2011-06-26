package com.github.zhongl.hs4j.kit.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;

/**
 * {@link HandlerSocket}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-3
 * 
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface HandlerSocket {
  public enum Command {
    INSERT, FIND, UPDATE, DELETE
  }

  Command value();
}
