
package com.cp.tools.sqlloader.dao.support;

import com.cp.tools.sqlloader.sql.util.SqlUtil;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.sql.Types;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ObjectTypeToSqlTypeConverter {

  private static final Log logger = LogFactory.getLog(ObjectTypeToSqlTypeConverter.class);

  private static Map<Class, Integer> DEFAULT_OBJECT_TYPE_TO_SQL_TYPE_MAP = new HashMap<Class, Integer>();
  private static Map<Class, Integer> USER_OBJECT_TYPE_TO_SQL_TYPE_MAP = new HashMap<Class, Integer>();

  static {
    DEFAULT_OBJECT_TYPE_TO_SQL_TYPE_MAP.put(Boolean.class, Types.BOOLEAN);
    DEFAULT_OBJECT_TYPE_TO_SQL_TYPE_MAP.put(Character.class, Types.CHAR);
    DEFAULT_OBJECT_TYPE_TO_SQL_TYPE_MAP.put(Calendar.class, Types.TIMESTAMP);
    DEFAULT_OBJECT_TYPE_TO_SQL_TYPE_MAP.put(Date.class, Types.DATE);
    DEFAULT_OBJECT_TYPE_TO_SQL_TYPE_MAP.put(Number.class, Types.NUMERIC);
    DEFAULT_OBJECT_TYPE_TO_SQL_TYPE_MAP.put(String.class, Types.VARCHAR);
    DEFAULT_OBJECT_TYPE_TO_SQL_TYPE_MAP = Collections.unmodifiableMap(DEFAULT_OBJECT_TYPE_TO_SQL_TYPE_MAP);
  }

  private ObjectTypeToSqlTypeConverter() {
  }

  public static int convert(final Class objectType) {
    if (ObjectUtil.isNotNull(objectType)) {
      final Integer sqlType = ObjectUtil.getDefaultValue(
        getSqlType(USER_OBJECT_TYPE_TO_SQL_TYPE_MAP, objectType),
        getSqlType(DEFAULT_OBJECT_TYPE_TO_SQL_TYPE_MAP, objectType));

      if (ObjectUtil.isNotNull(sqlType)) {
        if (logger.isDebugEnabled()) {
          logger.debug("converted Object type (" + objectType.getName() + ") to SQL type ("
            + SqlUtil.getSqlTypeDescription(sqlType) + ")");
        }

        return sqlType;
      }
      else {
        logger.warn("No SQL type is registered for Object type (" + objectType.getName() + ")!");
        throw new NoObjectTypeToSqlTypeRegisteredException("No SQL type is registered for Object type ("
          + objectType.getName() + ")!");
      }
    }
    else {
      return Types.NULL;
    }
  }

  protected static Integer getSqlType(final Map<Class, Integer> objectTypeToSqlTypeMap, final Class objectType ) {
    Assert.notNull(objectTypeToSqlTypeMap, "The specified Object type to SQL type mapping cannot be null!");
    Assert.notNull(objectType, "The Object type cannot be null!");

    final Integer sqlType = objectTypeToSqlTypeMap.get(objectType);

    if (ObjectUtil.isNull(sqlType)) {
      for (final Class registeredObjectType : objectTypeToSqlTypeMap.keySet()) {
        if (registeredObjectType.isAssignableFrom(objectType)) {
          return objectTypeToSqlTypeMap.get(registeredObjectType);
        }
      }
    }

    return sqlType;
  }

  public static boolean registerType(final Class type, final int sqlType) {
    Assert.notNull(type, "The Class type cannot be null!");
    USER_OBJECT_TYPE_TO_SQL_TYPE_MAP.put(type, sqlType);
    return (USER_OBJECT_TYPE_TO_SQL_TYPE_MAP.get(type) == sqlType);
  }

  public static void reset() {
    USER_OBJECT_TYPE_TO_SQL_TYPE_MAP.clear();
  }

  public static boolean unregisterType(final Class type) {
    return ObjectUtil.isNotNull(USER_OBJECT_TYPE_TO_SQL_TYPE_MAP.remove(type));
  }

}
