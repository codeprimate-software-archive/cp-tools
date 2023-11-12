
package com.cp.tools.sqlloader.sql;

import com.cp.tools.sqlloader.mapping.beans.TableDefinition;

// factory class for SQL statements to support the CRUD operations (Create, Read, Update, Delete).
public interface SqlDialectFactory {

  public String createDeleteSql(TableDefinition tableDefinition);

  public String createInsertSql(TableDefinition tableDefinition);

  public String createSelectSql(TableDefinition tableDefinition);

  public String createUpdateSql(TableDefinition tableDefinition);

}
