package com.github.zhongl.hs4j.kit.proxy;

import static com.github.zhongl.hs4j.kit.proxy.ResultSetGetters.*;
import static com.github.zhongl.hs4j.kit.util.ClassUtils.*;
import static com.github.zhongl.hs4j.kit.util.CollectionUtils.*;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

import net.vidageek.mirror.dsl.*;

import com.github.zhongl.hs4j.kit.annotations.*;
import com.github.zhongl.hs4j.kit.util.*;

/**
 * {@link ReturnType} supports:
 * <ul>
 * <li>Primitive or Wrapper;</li>
 * <li>{@link String} ;</li>
 * <li>Entity (or java bean, or POJO);</li>
 * <li>{@link Collection} of Entity.</li>
 * </ul>
 * 
 * Most unsupported types would be found in constructing of {@link ReturnType}, but some type-dismatching only could be
 * found in casting.
 * 
 * @author <a href=mailto:zhong.lunfu@gmail.com>zhongl</a>
 * @created 2011-6-25
 * 
 */
public class ReturnType {

  private static Object byteValue(ResultSet result) throws SQLException {
    return result.getByte(0);
  }

  private static String columnLabelOf(Field field) {
    final Column columnName = field.getAnnotation(Column.class);
    return (columnName != null) ? columnName.value() : field.getName();
  }

  private static Object doubleValue(ResultSet result) throws SQLException {
    return result.getDouble(0);
  }

  private static Object entity(Class<?> clazz, ResultSet result) throws SQLException {
    Mirror mirror = new Mirror();
    Object entity = mirror.on(clazz).invoke().constructor().bypasser();
    Field[] fields = clazz.getFields();
    for (Field field : fields) {
      Object value = resultSetGetterOf(field.getType()).get(result, columnLabelOf(field));
      mirror.on(entity).set().field(field).withValue(value);
    }
    return entity;
  }

  private static Object floatValue(ResultSet result) throws SQLException {
    return result.getFloat(0);
  }

  private static Object integerValue(ResultSet result) throws SQLException {
    return result.getInt(0);
  }

  private static boolean isIterableButNotCollection(Class<?> type) {
    return isIterable(type) && !ClassUtils.isCollection(type);
  }

  private static Object longValue(ResultSet result) throws SQLException {
    return result.getLong(0);
  }

  private static Object shortValue(ResultSet result) throws SQLException {
    return result.getShort(0);
  }

  private static Object stringValue(ResultSet result) throws SQLException {
    return result.getString(0);
  }

  public ReturnType(Method method) {
    type = method.getReturnType();
    assertNotArrayReturnType(method);
    assertNotCharacterReturnType(method);
    assertCollectionNotIterableReturnType(method);
    assertCollectionNotIteratorReturnType(method);
    isVoid = isIn(Void.class, Void.TYPE);
    genericType = getGenericTypeIfIsIterableReturnType(method);
  }

  public Object cast(boolean result) {
    return isVoid ? null : result;
  }

  public Object cast(int result) {
    return isVoid ? null : result;
  }

  public Object cast(ResultSet result) throws SQLException {
    return isVoid ? null : toUserFriendlyObject(result);
  }

  public boolean isIn(Class<?>... classes) {
    return set(classes).contains(type);
  }

  public boolean isNotIn(Class<?>... classes) {
    return !isIn(classes);
  }

  /**
   * @return true if type is single object but not primitive type or it wrapper or String.
   */
  public boolean isSingleEntity() {
    if (isCollection(type)) return false;
    if (isIn(Void.class, Void.TYPE,
             Boolean.class, Boolean.TYPE,
             Byte.class, Byte.TYPE,
             Short.class, Short.TYPE,
             Integer.class, Integer.TYPE,
             Long.class, Long.TYPE,
             Float.class, Float.TYPE,
             Double.class, Double.TYPE,
             String.class)) return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ReturnType [type=").append(type).append(", genericType=").append(genericType).append("]");
    return builder.toString();
  }

  private void assertCollectionNotIterableReturnType(Method method) {
    if (isIterableButNotCollection(type))
      throw new IllegalArgumentException("Please use Collection<E>, Iterable<E> return type is unsupported for "
          + method);
  }

  private void assertCollectionNotIteratorReturnType(Method method) {
    if (ClassUtils.isIterator(type))
      throw new IllegalArgumentException("Please use Collection<E>, Iterator<E> return type is unsupported for "
          + method);
  }

  private void assertNotArrayReturnType(Method method) {
    if (type.isArray()) throw new IllegalArgumentException("Array return type is unsupported for " + method);
  }

  private void assertNotCharacterReturnType(Method method) {
    if (isCharacter(type))
      throw new IllegalArgumentException("Character return type is unsupported for " + method);
  }

  private boolean booleanValue(ResultSet result) throws SQLException {
    return result.getBoolean(0);
  }

  private Object booleanValues(ResultSet result) throws SQLException {
    Collection<Object> values = newCollectionInstanceOfReturnType();
    while (result.next())
      values.add(booleanValue(result));
    return values;
  }

  private Object byteValues(ResultSet result) throws SQLException {
    Collection<Object> values = newCollectionInstanceOfReturnType();
    while (result.next())
      values.add(byteValue(result));
    return values;
  }

  private Object collection(ResultSet result) throws SQLException {
    if (isBoolean(genericType)) return booleanValues(result);
    if (isByte(genericType)) return byteValues(result);
    if (isShort(genericType)) return shortValues(result);
    if (isInteger(genericType)) return integerValues(result);
    if (isLong(genericType)) return longValues(result);
    if (isFloat(genericType)) return floatValues(result);
    if (isDouble(genericType)) return doubleValues(result);
    if (isString(genericType)) return stringValues(result);
    return entityValues(result);
  }

  private Object doubleValues(ResultSet result) throws SQLException {
    Collection<Object> values = newCollectionInstanceOfReturnType();
    while (result.next())
      values.add(doubleValue(result));
    return values;
  }

  private Object entityValue(ResultSet result) throws SQLException {
    return entity(type, result);
  }

  private Object entityValues(ResultSet result) throws SQLException {
    Collection<Object> values = newCollectionInstanceOfReturnType();
    while (result.next())
      values.add(entity(genericType, result));
    return values;
  }

  private Object floatValues(ResultSet result) throws SQLException {
    Collection<Object> values = newCollectionInstanceOfReturnType();
    while (result.next())
      values.add(floatValue(result));
    return values;
  }

  private Class<?> getGenericTypeIfIsIterableReturnType(Method method) {
    if (!isIterable(type)) return null;
    Type genericReturnType = method.getGenericReturnType();
    if (!isParameterizedType(genericReturnType))
      throw new IllegalArgumentException("Non-Parameterized iterable type is unsupported for " + method);
    return getOnlyOneTypeArgumentClassFrom(parameterized(genericReturnType));
  }

  private Object integerValues(ResultSet result) throws SQLException {
    Collection<Object> values = newCollectionInstanceOfReturnType();
    while (result.next())
      values.add(integerValue(result));
    return values;
  }

  private Object longValues(ResultSet result) throws SQLException {
    Collection<Object> values = newCollectionInstanceOfReturnType();
    while (result.next())
      values.add(longValue(result));
    return values;
  }

  private Collection<Object> newCollectionInstanceOfReturnType() {
    if (isIn(Set.class)) return new HashSet<Object>();
    if (isIn(List.class, Collection.class)) return new ArrayList<Object>();
    throw new IllegalStateException(type + " is unsupported, please use Collection<E> or List<E> or Set<E>");
  }

  private Object shortValues(ResultSet result) throws SQLException {
    Collection<Object> values = newCollectionInstanceOfReturnType();
    while (result.next())
      values.add(shortValue(result));
    return values;
  }

  private Object singleton(ResultSet result) throws SQLException {
    if (isBoolean(type)) return booleanValue(result);
    if (isByte(type)) return byteValue(result);
    if (isShort(type)) return shortValue(result);
    if (isInteger(type)) return integerValue(result);
    if (isLong(type)) return longValue(result);
    if (isFloat(type)) return floatValue(result);
    if (isDouble(type)) return doubleValue(result);
    if (isString(type)) return stringValue(result);
    return entityValue(result);
  }

  private Object stringValues(ResultSet result) throws SQLException {
    Collection<Object> values = newCollectionInstanceOfReturnType();
    while (result.next())
      values.add(stringValue(result));
    return values;
  }

  private Object toUserFriendlyObject(ResultSet result) throws SQLException {
    if (isCollection(type)) return collection(result);
    return singleton(result);
  }

  private final Class<?> genericType;
  private final boolean isVoid;
  private final Class<?> type;

}
