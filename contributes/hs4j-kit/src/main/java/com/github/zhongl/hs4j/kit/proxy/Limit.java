package com.github.zhongl.hs4j.kit.proxy;

import java.lang.annotation.*;
import java.util.*;

import com.github.zhongl.hs4j.kit.annotations.*;
import com.github.zhongl.hs4j.kit.arguments.*;
import com.github.zhongl.hs4j.kit.arguments.ParameterAnnotations.ApplyableCollector;

/**
 * {@link Limit}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-26
 * 
 */
class Limit {
  public Limit(ParameterAnnotations parameterAnnotations, ReturnType returnType) {
    if (returnType.isSingleEntity()) {
      limitCollector = new ConstantIntegerCollector(1);
      offsetCollector = new ConstantIntegerCollector(0);
    } else {
      limitCollector = parameterAnnotations.apply(new SingleOrNoneIntegerCollector(Limit.class, Integer.MAX_VALUE));
      offsetCollector = parameterAnnotations.apply(new SingleOrNoneIntegerCollector(Offset.class, 0));
    }
  }

  public final Collector<Integer> limitCollector;
  public final Collector<Integer> offsetCollector;

  private static class ConstantIntegerCollector implements Collector<Integer> {

    public ConstantIntegerCollector(Integer value) {
      this.value = value;
    }

    @Override
    public Integer collectFrom(Object[] args) {
      return value;
    }

    private final Integer value;

  }

  private static class SingleOrNoneIntegerCollector extends ApplyableCollector<Integer> {

    public SingleOrNoneIntegerCollector(Class<?> clazz, int defaultValue) {
      super("At lease one argument should be annotated by @" + clazz.getSimpleName());
      this.clazz = clazz;
      this.defaultValue = defaultValue;
    }

    @Override
    protected boolean apply(Annotation annotation) {
      return annotation.getClass().isAssignableFrom(clazz);
    }

    @Override
    protected Integer collectFrom(List<Object> values) {
      return values.isEmpty() ? defaultValue : (Integer) values.get(0);
    }

    @Override
    protected boolean invalid(int size) {
      return size > 1;
    }

    private final Class<?> clazz;
    private final int defaultValue;

  }

}
