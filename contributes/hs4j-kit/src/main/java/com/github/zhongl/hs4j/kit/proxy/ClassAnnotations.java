package com.github.zhongl.hs4j.kit.proxy;

import static com.github.zhongl.hs4j.kit.util.AssertUtils.*;

import java.lang.reflect.*;

import com.github.zhongl.hs4j.kit.annotations.*;

/**
 * {@link ClassAnnotations}
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-25
 * 
 */
class ClassAnnotations {

  private static String[] getColumnsFrom(Class<?> clazz) {
    return getColumnsFrom(clazz.getFields());
  }

  private static String[] getColumnsFrom(Field[] fields) {
    final String[] columns = new String[fields.length];
    for (int i = 0; i < columns.length; i++) {
      final Column column = fields[i].getAnnotation(Column.class);
      columns[i] = (isEmpty(column)) ? fields[i].getName() : column.value();
    }
    return columns;
  }

  public ClassAnnotations(Class<?> clazz) {
    this.clazz = clazz;
    table = clazz.getAnnotation(Table.class);
    columns = clazz.getAnnotation(Columns.class);
    entity = clazz.getAnnotation(Entity.class);
    index = clazz.getAnnotation(Index.class);
  }

  public String[] getColumnsOrThrowExceptionWith(Method method) {
    if (isEmpty(entity) && isEmpty(columns))
      throw new IllegalArgumentException(method + "should be annotated with @Columns");
    if (isNotEmpty(columns) && isNotEmpty(columns.value())) return columns.value();
    return getColumnsFrom(entity.value());
  }

  public String getIndexName() {
    return isEmpty(index) ? "PRIMARY" : index.value();
  }

  public String getTable() {
    if (isEmpty(table)) throw new IllegalArgumentException(clazz + " should be annotated with @Table");
    return table.value();
  }

  private final Class<?> clazz;
  private final Table table;
  private final Columns columns;
  private final Entity entity;
  private final Index index;
}
