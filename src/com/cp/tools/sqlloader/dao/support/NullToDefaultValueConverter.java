
package com.cp.tools.sqlloader.dao.support;

import com.cp.common.beans.util.converters.AbstractConverter;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Resettable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class NullToDefaultValueConverter extends AbstractConverter implements Resettable {

  public static final NullToDefaultValueConverter INSTANCE = new NullToDefaultValueConverter();

  private static final Map<Class, Object> DEFAULT_VALUE_MAP = new HashMap<Class, Object>(7);
  private static final Map<Class, Object> USER_DEFAULT_VALUE_MAP = new HashMap<Class, Object>();

  static {
    DEFAULT_VALUE_MAP.put(BigInteger.class, new BigInteger("0"));
    DEFAULT_VALUE_MAP.put(BigDecimal.class, new BigDecimal("0.0"));
    DEFAULT_VALUE_MAP.put(Boolean.class, Boolean.FALSE);
    DEFAULT_VALUE_MAP.put(Byte.class, (byte) 0);
    DEFAULT_VALUE_MAP.put(Calendar.class, CalendarValueFactory.INSTANCE);
    DEFAULT_VALUE_MAP.put(Character.class, '?');
    DEFAULT_VALUE_MAP.put(Date.class, DateValueFactory.INSTANCE);
    DEFAULT_VALUE_MAP.put(Double.class, 0.0d);
    DEFAULT_VALUE_MAP.put(Float.class, 0.0f);
    DEFAULT_VALUE_MAP.put(Integer.class, 0);
    DEFAULT_VALUE_MAP.put(Long.class, 0l);
    DEFAULT_VALUE_MAP.put(Short.class, (short) 0);
    DEFAULT_VALUE_MAP.put(String.class, "");
  }

  private NullToDefaultValueConverter() {
  }

  protected Object convertImpl(final Class type, final Object value) {
    if (ObjectUtil.isNull(value)) {
      return ObjectUtil.getDefaultValue(getDefaultValue(USER_DEFAULT_VALUE_MAP, type),
        getDefaultValue(DEFAULT_VALUE_MAP, type));
    }
    else {
      return value;
    }
  }

  protected Object getDefaultValue(final Map<Class, Object> defaultValueMap, final Class type) {
    Assert.notNull(defaultValueMap, "The default value mapping cannot be null!");
    Assert.notNull(type, "The Class type of the default value must be specified!");

    Object defaultValue = defaultValueMap.get(type);

    if (ObjectUtil.isNull(defaultValue)) {
      for (final Class registeredType : defaultValueMap.keySet()) {
        if (registeredType.isAssignableFrom(type)) {
          defaultValue = defaultValueMap.get(registeredType);
          break;
        }
      }
    }

    if (defaultValue instanceof ValueFactory) {
      defaultValue = ((ValueFactory) defaultValue).getValue();
    }

    return defaultValue;
  }

  public static <T> boolean registerDefaultValue(final Class<T> type, final T defaultValue) {
    Assert.notNull(type, "The Class type of the default value cannot be null!");
    Assert.isTrue(type.isInstance(defaultValue), "The Class type (" + ClassUtil.getClassName(defaultValue)
      + ") of the default value must be assignable to (" + type.getName() + ")!");
    USER_DEFAULT_VALUE_MAP.put(type, defaultValue);
    return USER_DEFAULT_VALUE_MAP.get(type).equals(defaultValue);
  }

  public static <T> boolean registerDefaultValue(final Class<T> type, final ValueFactory<T> factory) {
    Assert.notNull(type, "The Class type of the default value cannot be null!");
    Assert.notNull(factory, "The ValueFactory cannot be null!");
    USER_DEFAULT_VALUE_MAP.put(type, factory);
    return USER_DEFAULT_VALUE_MAP.get(type).equals(factory);
  }

  public void reset() {
    USER_DEFAULT_VALUE_MAP.clear();
  }

  public static boolean unregisterDefaultValue(final Class type) {
    return ObjectUtil.isNotNull(USER_DEFAULT_VALUE_MAP.remove(type));
  }

  public static interface ValueFactory<T> {

    public T getValue();

  }

  protected static final class CalendarValueFactory implements ValueFactory<Calendar> {

    public static final CalendarValueFactory INSTANCE = new CalendarValueFactory();

    public Calendar getValue() {
      return Calendar.getInstance();
    }
  }

  protected static final class DateValueFactory implements ValueFactory<Date> {

    public static final DateValueFactory INSTANCE = new DateValueFactory();

    public Date getValue() {
      return Calendar.getInstance().getTime();
    }
  }

}
