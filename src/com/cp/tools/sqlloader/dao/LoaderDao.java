
package com.cp.tools.sqlloader.dao;

import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.common.util.record.RecordTable;
import javax.sql.DataSource;

public interface LoaderDao {

  public DataSource getDataSource();

  public void persist(final TableDefinition tableDefinition, final RecordTable data);

}
