
package com.cp.tools.sqlloader.dao.support;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ObjectTypeToJdbcTypeConverter {

  private static final Converter DEFAULT_CONVERTER = new DefaultConverter();

  private static final Log logger = LogFactory.getLog(ObjectTypeToJdbcTypeConverter.class);

  private static Map<Class, Converter> DEFAULT_OBJECT_TYPE_TO_JDBC_TYPE_CONVERTERS = new HashMap<Class, Converter>(2);
  private static Map<Class, Converter> USER_OBJECT_TYPE_TO_JDBC_TYPE_CONVERTERS = new HashMap<Class, Converter>();

  static {
    DEFAULT_OBJECT_TYPE_TO_JDBC_TYPE_CONVERTERS.put(Calendar.class, CalendarToTimestampConverter.INSTANCE);
    DEFAULT_OBJECT_TYPE_TO_JDBC_TYPE_CONVERTERS = Collections.unmodifiableMap(DEFAULT_OBJECT_TYPE_TO_JDBC_TYPE_CONVERTERS);
  }

  private ObjectTypeToJdbcTypeConverter() {
  }

  public static Object convert(final Object value) {
    return convert(ClassUtil.getClass(value), value);
  }

  public static Object convert(final Class objectType, final Object value) {
    if (ObjectUtil.isNotNull(value)) {
      final Converter converter = ObjectUtil.getDefaultValue(
        getConverter(USER_OBJECT_TYPE_TO_JDBC_TYPE_CONVERTERS, objectType),
        getConverter(DEFAULT_OBJECT_TYPE_TO_JDBC_TYPE_CONVERTERS, objectType),
        DEFAULT_CONVERTER);

      final Object convertedValue = converter.convert(objectType, value);

      if (logger.isDebugEnabled()) {
        logger.debug("converted value (" + value + ") of object type (" + objectType + ") to (" + convertedValue + ")");
      }

      return convertedValue;
    }
    else {
      logger.warn("value was null; no conversion necessary!");
      return null;
    }
  }

  protected static Converter getConverter(final Map<Class, Converter> objectTypeToJdbcTypeConverters,
                                          final Class objectType)
  {
    Assert.notNull(objectTypeToJdbcTypeConverters, "The Object type to JDBC type mapping cannot be null!");
    Assert.notNull(objectType, "The Object type cannot be null!");

    final Converter converter = objectTypeToJdbcTypeConverters.get(objectType);

    if (ObjectUtil.isNull(converter)) {
      for (final Class registeredObjectType : objectTypeToJdbcTypeConverters.keySet()) {
        if (registeredObjectType.isAssignableFrom(objectType)) {
          return objectTypeToJdbcTypeConverters.get(registeredObjectType);
        }
      }
    }

    return converter;
  }

  public static boolean registerConverter(final Class type, final Converter converter) {
    Assert.notNull(type, "The Class type of the object to register the Converter for cannot be null!");
    Assert.notNull(converter, "The Converter used to convert objects of the specified Class type cannot be null!");
    USER_OBJECT_TYPE_TO_JDBC_TYPE_CONVERTERS.put(type, converter);
    return converter.equals(USER_OBJECT_TYPE_TO_JDBC_TYPE_CONVERTERS.get(type));
  }

  public static void reset() {
    USER_OBJECT_TYPE_TO_JDBC_TYPE_CONVERTERS.clear();
  }

  public static boolean unregisterConverter(final Class type) {
    return ObjectUtil.isNotNull(USER_OBJECT_TYPE_TO_JDBC_TYPE_CONVERTERS.remove(type));
  }

  private static final class DefaultConverter implements Converter {

    public Object convert(final Class type, final Object value) {
      return value;
    }
  }

}
