
package com.cp.tools.sqlloader.sql.support;

import com.cp.tools.sqlloader.mapping.beans.DataSourceDefinition;
import com.cp.tools.sqlloader.sql.AbstractDelegatingDataSource;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DriverManagerDelegatingDataSource extends AbstractDelegatingDataSource {

  @Override
  public DataSource getDataSource(final DataSourceDefinition dataSourceDefinition) {
    final String jdbcDriver = dataSourceDefinition.getJdbcDriver();
    final String jdbcUrl = dataSourceDefinition.getJdbcUrl();
    final String jdbcUsername = dataSourceDefinition.getJdbcUsername();
    final String jdbcPassword = dataSourceDefinition.getJdbcPassword();

    if (logger.isDebugEnabled()) {
      logger.debug("JDBC Driver (" + jdbcDriver + ")");
      logger.debug("JDBC URL (" + jdbcUrl + ")");
      logger.debug("JDBC Username (" + jdbcUsername + ")");
      logger.debug("JDBC Password (" + jdbcPassword + ")");
    }

    return new DriverManagerDataSource(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
  }

}
