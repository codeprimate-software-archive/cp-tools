
package com.cp.tools.sqlloader.sql;

import com.cp.tools.sqlloader.mapping.beans.DataSourceDefinition;
import javax.sql.DataSource;

public interface DelegatingDataSource extends DataSource {

  public void init(DataSourceDefinition dataSourceDefinition);

}
