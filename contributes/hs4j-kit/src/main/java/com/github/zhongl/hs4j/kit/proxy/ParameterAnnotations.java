package com.github.zhongl.hs4j.kit.proxy;

import static com.github.zhongl.hs4j.kit.util.AssertUtils.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import com.github.zhongl.hs4j.kit.annotations.*;

/**
 * {@link ParameterAnnotations}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-3
 * 
 */
public class ParameterAnnotations {

  public ParameterAnnotations(Method method) {
    this.method = method;
    entity = method.getDeclaringClass().getAnnotation(Entity.class);
  }

  public <T> Collector<T> apply(ApplyableCollector<T> applyableCollector) {
    final Annotation[][] parameterAnnotations = method.getParameterAnnotations();;
    for (int x = 0; x < parameterAnnotations.length; x++) {
      if (isEmpty(parameterAnnotations[x]) && applyableCollector.apply(x, null)) continue;
      for (Annotation annotation : parameterAnnotations[x]) 
        applyableCollector.apply(x, annotation);
    }
    applyableCollector.validate();
    return applyableCollector;
  }

  public boolean isEntityTypeParameter() {
    if (isEmpty(entity)) return false;
    Class<?>[] classes = method.getParameterTypes();
    if (entityNotIn(classes)) return false;
    if (classes.length == 1) return true;
    throw new IllegalArgumentException("Parameter should be only one if it's type is " + entity.value());
  }

  private boolean entityNotIn(Class<?>[] classes) {
    for (Class<?> clazz : classes)
      if (clazz.equals(entity.value())) return false;
    return true;
  }

  private final Method method;
  private final Entity entity;

  public static abstract class ApplyableCollector<T> implements Collector<T> {
    public ApplyableCollector(String invalidMessage) {
      this.invalidMessage = invalidMessage;
      indexs = new ArrayList<Integer>();
    }

    @Override
    public final T collectFrom(final Object[] args) {
      return collectFrom(new AbstractList<Object>() {

        @Override
        public Object get(int index) {
          return args[indexs.get(index)];
        }

        @Override
        public int size() {
          return indexs.size();
        }
      });
    }

    protected abstract boolean apply(Annotation annotation);

    protected abstract T collectFrom(List<Object> values);

    protected abstract boolean invalid(int size);

    private boolean apply(int index, Annotation annotation) {
      return apply(annotation) && indexs.add(index);
    }

    private void validate() {
      if (invalid(indexs.size())) throw new IllegalArgumentException(invalidMessage);
    }

    private final String invalidMessage;
    protected final List<Integer> indexs;
  }
}
