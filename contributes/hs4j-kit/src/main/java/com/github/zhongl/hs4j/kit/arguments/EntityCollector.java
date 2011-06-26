package com.github.zhongl.hs4j.kit.arguments;

import static com.github.zhongl.hs4j.kit.util.StringUtils.*;

import java.lang.reflect.*;

import net.vidageek.mirror.dsl.*;

/**
 * {@link EntityCollector} supposes :
 * <ul>
 * <li> the order of fields as same as columns in the table, 
 * <li> the first field as the {@code PRIMARY} key, 
 * <li> every field's toString result as value.
 * </ul>
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-5
 * 
 */
public class EntityCollector implements Collector<String[]> {


  public EntityCollector(CollectStrategy strategy) {
    this.strategy = strategy;
  }

  @Override
  public String[] collectFrom(Object[] args) {
    final Object object = args[0];
    final Mirror mirror = new Mirror();
    final Field[] fields = object.getClass().getFields();
    final Object[] fieldValues = new Object[fields.length];

    for (int i = 0; i < fields.length; i++) {
      fieldValues[i] = mirror.on(object).get().field(fields[i]);
    }
    return strategy.collectFrom(fieldValues);
  }

  private final CollectStrategy strategy;

  public enum CollectStrategy {
    /** Default as one primary key.  */
    KEYS {
      @Override
      String[] collectFrom(Object[] fieldValues) {
        return toStringArray(fieldValues[PRIMARY]);
      }
    },
    VALUES {
      @Override
      String[] collectFrom(Object[] fieldValues) {
        return toStringArray(fieldValues, PRIMARY + 1, fieldValues.length);
      }
    },
    ALL {
      @Override
      String[] collectFrom(Object[] fieldValues) {
        return toStringArray(fieldValues);
      }
    };
    abstract String[] collectFrom(Object[] fieldValues);
    private static final int PRIMARY = 0;
  }
}
