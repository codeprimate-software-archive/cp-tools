
package com.cp.tools.sqlloader.sql;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.tools.sqlloader.mapping.beans.DataSourceDefinition;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.cp.common.lang.UtilityMethods.is;

public abstract class AbstractDelegatingDataSource implements DelegatingDataSource {

  protected final Log logger = LogFactory.getLog(getClass());

  private DataSource delegatingDataSource;

  private DataSourceDefinition dataSourceDefinition;

  public void init(final DataSourceDefinition dataSourceDefinition) {
    Assert.notNull(dataSourceDefinition, "The data source definition for this delegating DataSource ("
      + getClass().getName() + ") object cannot be null!");

    if (is(ObjectUtil.equals(this.dataSourceDefinition, dataSourceDefinition)).False()) {
      setDataSource(getDataSource(dataSourceDefinition));
      this.dataSourceDefinition = dataSourceDefinition;
    }
  }

  protected DataSource getDataSource() {
    Assert.state(ObjectUtil.isNotNull(delegatingDataSource), "The DataSource for this delegate (" + getClass().getName()
      + ") was not properlty initialized!");
    return delegatingDataSource;
  }

  protected abstract DataSource getDataSource(DataSourceDefinition dataSourceDefinition);

  protected void setDataSource(final DataSource delegatingDataSource) {
    Assert.notNull(delegatingDataSource, "The data source to delegate JDBC operations to cannot be null!");
    this.delegatingDataSource = delegatingDataSource;
  }

  protected DataSourceDefinition getDataSourceDefinition() {
    return dataSourceDefinition;
  }

  public Connection getConnection() throws SQLException {
    return getDataSource().getConnection();
  }

  public Connection getConnection(final String username, final String password) throws SQLException {
    return getDataSource().getConnection(username, password);
  }

  public int getLoginTimeout() throws SQLException {
    return getDataSource().getLoginTimeout();
  }

  public void setLoginTimeout(final int seconds) throws SQLException {
    getDataSource().setLoginTimeout(seconds);
  }

  public PrintWriter getLogWriter() throws SQLException {
    return getDataSource().getLogWriter();
  }

  public void setLogWriter(final PrintWriter out) throws SQLException {
    getDataSource().setLogWriter(out);
  }

  public boolean isWrapperFor(final Class<?> iface) throws SQLException {
    return getDataSource().getClass().equals(iface);
  }

  public <T> T unwrap(final Class<T> iface) throws SQLException {
    return (T) getDataSource();
  }

}
