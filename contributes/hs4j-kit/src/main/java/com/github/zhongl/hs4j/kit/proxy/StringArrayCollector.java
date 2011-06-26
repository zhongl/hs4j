package com.github.zhongl.hs4j.kit.proxy;

import static com.github.zhongl.hs4j.kit.util.AssertUtils.*;
import static com.github.zhongl.hs4j.kit.util.StringUtils.*;

import java.lang.annotation.*;
import java.util.*;

import com.github.zhongl.hs4j.kit.annotations.*;
import com.github.zhongl.hs4j.kit.arguments.ParameterAnnotations.ApplyableCollector;

/**
 * {@link StringArrayCollector}
 * @author  <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-26
 * 
 */
abstract class StringArrayCollector extends ApplyableCollector<String[]> {

  public StringArrayCollector(String invalidMessage) {
    super(invalidMessage);
  }

  @Override
  protected String[] collectFrom(List<Object> values) {
    return toStringArray(values);
  }

  @Override
  protected boolean invalid(int size) {
    return size < 1;
  }
}

/**
 * {@link ValuesCollector}
 * @author  <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-26
 * 
 */
class ValuesCollector extends StringArrayCollector {

  public ValuesCollector() {
    super("At lease one argument without annotation is needed as value.");
  }

  @Override
  protected boolean apply(Annotation annotation) {
    return isEmpty(annotation);
  }
  
}

/**
 * {@link KeysCollector}
 * @author  <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-26
 * 
 */
class KeysCollector extends StringArrayCollector {

  public KeysCollector() {
    super("At lease one argument annotated by @key");
  }

  @Override
  protected boolean apply(Annotation annotation) {
    return annotation instanceof Key;
  }

}