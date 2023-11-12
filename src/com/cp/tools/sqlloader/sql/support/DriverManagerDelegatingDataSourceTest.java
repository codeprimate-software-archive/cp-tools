
package com.cp.tools.sqlloader.sql.support;

import com.cp.tools.sqlloader.mapping.beans.DataSourceDefinition;
import javax.sql.DataSource;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DriverManagerDelegatingDataSourceTest extends TestCase {

  private final Mockery context = new Mockery();

  public DriverManagerDelegatingDataSourceTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DriverManagerDelegatingDataSourceTest.class);
    return suite;
  }

  public void testGetDataSource() throws Exception {
    final DriverManagerDelegatingDataSource delegatingDataSource = new DriverManagerDelegatingDataSource();

    final DataSourceDefinition mockDataSourceDefinition = context.mock(DataSourceDefinition.class);

    context.checking(new Expectations() {{
      one(mockDataSourceDefinition).getJdbcDriver();
      //will(returnValue("mock.jdbc.driver.MockDriver"));
      will(returnValue("sun.jdbc.odbc.JdbcOdbcDriver"));
      one(mockDataSourceDefinition).getJdbcUrl();
      will(returnValue("jdbc:mock:mockSchema@mockDb:2120:mockServer"));
      one(mockDataSourceDefinition).getJdbcUsername();
      will(returnValue("theUsername"));
      one(mockDataSourceDefinition).getJdbcPassword();
      will(returnValue("thePassword"));
    }});

    final DataSource dataSource = delegatingDataSource.getDataSource(mockDataSourceDefinition);

    assertNotNull(dataSource);
    assertTrue(dataSource instanceof DriverManagerDataSource);

    final DriverManagerDataSource driverManagerDataSource = (DriverManagerDataSource) dataSource;

    // TODO consider implementing my own DriverManagerDataSource
    //assertEquals("sun.jdbc.odbc.JdbcOdbcDriver", driverManagerDataSource.getDriverClassName());
    assertEquals("jdbc:mock:mockSchema@mockDb:2120:mockServer", driverManagerDataSource.getUrl());
    assertEquals("theUsername", driverManagerDataSource.getUsername());
    assertEquals("thePassword", driverManagerDataSource.getPassword());
  }

}
