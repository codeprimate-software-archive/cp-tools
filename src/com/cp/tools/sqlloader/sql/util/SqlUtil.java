
package com.cp.tools.sqlloader.sql.util;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.common.lang.Assert;
import java.sql.Types;

public final class SqlUtil {

  private SqlUtil() {
  }

  public static String[] getParameterNames(final TableDefinition tableDefinition) {
    Assert.notNull(tableDefinition, "The table definition cannot be null!");

    final String[] parameterNames = new String[tableDefinition.getColumnDefinitionCount()];
    int index = 0;

    for (final ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitions()) {
      parameterNames[index++] = columnDefinition.getProperty();
    }

    return parameterNames;
  }

  public static String getSqlTypeDescription(final int sqlType) {
    switch (sqlType) {
      case Types.ARRAY:
        return "SQL ARRAY";
      case Types.BIGINT:
        return "SQL BIGINT";
      case Types.BINARY:
        return "SQL BINARY";
      case Types.BIT:
        return "SQL BIT";
      case Types.BLOB:
        return "SQL BLOB";
      case Types.BOOLEAN:
        return "SQL BOOLEAN";
      case Types.CHAR:
        return "SQL CHAR";
      case Types.CLOB:
        return "SQL CLOB";
      case Types.DATALINK:
        return "SQL DATALINK";
      case Types.DATE:
        return "SQL DATE";
      case Types.DECIMAL:
        return "SQL DECIMAL";
      case Types.DOUBLE:
        return "SQL DOUBLE";
      case Types.FLOAT:
        return "SQL FLOAT";
      case Types.INTEGER:
        return "SQL INTEGER";
      case Types.JAVA_OBJECT:
        return "SQL JAVA OBJECT";
      case Types.LONGVARBINARY:
        return "SQL LONGVARBINARY";
      case Types.LONGVARCHAR:
        return "SQL LONGVARCHAR";
      case Types.NULL:
        return "SQL NULL";
      case Types.NUMERIC:
        return "SQL NUMERIC";
      case Types.OTHER:
        return "SQL OTHER";
      case Types.REAL:
        return "SQL REAL";
      case Types.REF:
        return "SQL REF";
      case Types.SMALLINT:
        return "SQL SMALLINT";
      case Types.STRUCT:
        return "SQL STRUCT";
      case Types.TIME:
        return "SQL TIME";
      case Types.TIMESTAMP:
        return "SQL TIMESTAMP";
      case Types.TINYINT:
        return "SQL TINYINT";
      case Types.VARBINARY:
        return "SQL VARBINARY";
      case Types.VARCHAR:
        return "SQL VARCHAR";
      default:
        return "SQL TYPE UNKNOWN!";
    }
  }

}
