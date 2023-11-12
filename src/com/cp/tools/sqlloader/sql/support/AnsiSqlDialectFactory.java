
package com.cp.tools.sqlloader.sql.support;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.tools.sqlloader.sql.AbstractSqlDialectFactory;
import com.cp.common.lang.Assert;
import com.cp.common.lang.StringUtil;

public class AnsiSqlDialectFactory extends AbstractSqlDialectFactory {

  protected static final String INSERT_SQL = "INSERT INTO $tableName ($columns) VALUES ($values)";

  @Override
  public String createInsertSql(final TableDefinition tableDefinition) {
    Assert.notNull(tableDefinition, "The table definition used to create the INSERT SQL statement cannot be null!");

    final StringBuffer columns = new StringBuffer();
    final StringBuffer values = new StringBuffer();

    String delimiter = "";

    for (final ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitions()) {
      columns.append(delimiter).append(columnDefinition.getName());
      values.append(delimiter).append(":").append(columnDefinition.getProperty());
      delimiter = ", ";
    }

    String insertSql = StringUtil.replace(INSERT_SQL, TABLE_NAME_PLACEHOLDER, tableDefinition.getName());
    insertSql = StringUtil.replace(insertSql, COLUMNS_PLACEHOLDER, columns.toString());
    insertSql = StringUtil.replace(insertSql, VALUES_PLACEHOLDER, values.toString());

    return insertSql;
  }

}
