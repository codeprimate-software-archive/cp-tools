
package com.cp.tools.sqlloader.mapping.beans;

import java.util.List;

public interface DataSourceDefinition {

  public String getJdbcDriver();

  public void setJdbcDriver(String jdbcDriver);

  public String getJdbcPassword();

  public void setJdbcPassword(String jdbcPassword);

  public String getJdbcUrl();

  public void setJdbcUrl(String jdbcUrl);

  public String getJdbcUsername();

  public void setJdbcUsername(String jdbcUsername);

  public String getName();

  public int getTableDefinitionCount();

  public List<TableDefinition> getTableDefinitions();

  public boolean add(TableDefinition tableDefinition);

}
