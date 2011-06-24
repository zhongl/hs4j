package com.github.zhongl.hs4j.kit.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;

import com.google.code.hs4j.*;

/**
 * {@link Update}
 * @author  <a href=mailto:jushi@taobao.com>jushi</a>
 * @created Jun 24, 2011
 * 
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface Update {
  FindOperator value() default FindOperator.EQ;
}
