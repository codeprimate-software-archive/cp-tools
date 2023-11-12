
package com.cp.tools.sqlloader.sql;

import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractSqlDialectFactory implements SqlDialectFactory {

  protected static final String COLUMNS_PLACEHOLDER = "$columns";
  protected static final String TABLE_NAME_PLACEHOLDER = "$tableName";
  protected static final String VALUES_PLACEHOLDER = "$values";

  protected final Log logger = LogFactory.getLog(getClass());

  public String createDeleteSql(TableDefinition tableDefinition) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public String createInsertSql(final TableDefinition tableDefinition) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public String createSelectSql(final TableDefinition tableDefinition) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public String createUpdateSql(final TableDefinition tableDefinition) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
