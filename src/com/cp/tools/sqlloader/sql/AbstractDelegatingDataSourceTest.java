
package com.cp.tools.sqlloader.sql;

import com.cp.tools.sqlloader.mapping.beans.DataSourceDefinition;
import com.cp.common.lang.ObjectUtil;
import javax.sql.DataSource;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class AbstractDelegatingDataSourceTest extends TestCase {

  private final Mockery context = new Mockery();

  public AbstractDelegatingDataSourceTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractDelegatingDataSourceTest.class);
    return suite;
  }

  protected AbstractDelegatingDataSource getDelegatingDataSource(final DataSource dataSource) {
    return new MockDelegatingDataSource(dataSource);
  }

  public void testInit() throws Exception {
    final DataSource mockDataSource = context.mock(DataSource.class);
    final DataSourceDefinition mockDataSourceDefinition = context.mock(DataSourceDefinition.class);
    final AbstractDelegatingDataSource delegatingDataSource = getDelegatingDataSource(mockDataSource);

    try {
      delegatingDataSource.init(mockDataSourceDefinition);
    }
    catch (Exception e) {
      fail("Calling init with a non-null DataSourceDefinition should not have thrown an Exception (" + e.getMessage() + ")!");
    }

    assertSame(mockDataSource, delegatingDataSource.getDataSource());
    assertSame(mockDataSourceDefinition, delegatingDataSource.getDataSourceDefinition());
  }

  public void testInitWithExistingDataSourceDefinition() throws Exception {
    final DataSource mockDataSource = context.mock(DataSource.class);

    final DataSourceDefinition existingMockDataSourceDefinition = context.mock(DataSourceDefinition.class, "existingMockDataSourceDefinition");
    final DataSourceDefinition newMockDataSourceDefinition = context.mock(DataSourceDefinition.class, "newMockDataSourceDefinition");

    context.checking(new Expectations() {{
      one(newMockDataSourceDefinition).getJdbcDriver();
      will(returnValue("mock.jdbc.drive.MockDriver"));
      one(newMockDataSourceDefinition).getJdbcPassword();
      will(returnValue("thePassword"));
      one(newMockDataSourceDefinition).getJdbcUrl();
      will(returnValue("jdbc.mock.mockSchema@mockDb:2122:mockServer"));
      one(newMockDataSourceDefinition).getJdbcUsername();
      will(returnValue("theUsername"));
      one(newMockDataSourceDefinition).getName();
      will(returnValue("mockDataSource"));
    }});

    final AbstractDelegatingDataSource delegatingDataSource = getDelegatingDataSource(mockDataSource);

    try {
      delegatingDataSource.init(existingMockDataSourceDefinition);
    }
    catch (Exception e) {
      fail("Setting the existing mock DataSourceDefinition object on the AbstractDelegatingDataSource class threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertSame(existingMockDataSourceDefinition, delegatingDataSource.getDataSourceDefinition());

    try {
      delegatingDataSource.init(newMockDataSourceDefinition);
    }
    catch (Exception e) {
      fail("Setting the new mock DataSourceDefinition object on the AbstractDelegatingDataSource class threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertSame(newMockDataSourceDefinition, delegatingDataSource.getDataSourceDefinition());
  }

  public void testInitThrowsException() throws Exception {
    final AbstractDelegatingDataSource delegatingDataSource = getDelegatingDataSource(null);

    try {
      delegatingDataSource.init(null);
      fail("Calling init with a null DataSourceDefinition object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The data source definition for this delegating DataSource (" + MockDelegatingDataSource.class.getName()
        + ") object cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling init with a null DataSourceDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testGetNullDataSourceThrowsException() throws Exception {
    final AbstractDelegatingDataSource delegatingDataSource = getDelegatingDataSource(null);
    DataSource dataSource = null;

    try {
      dataSource = delegatingDataSource.getDataSource();
      fail("Calling getDataSource on AbstractDelegatingDataSource having a null dataSource property should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The DataSource for this delegate (" + MockDelegatingDataSource.class.getName()
        + ") was not properlty initialized!", e.getMessage());
    }

    assertNull(dataSource);
  }

  public void testSetDataSource() throws Exception {
    final DataSource mockDataSource = context.mock(DataSource.class);
    final AbstractDelegatingDataSource delegatingDataSource = getDelegatingDataSource(null);

    try {
      delegatingDataSource.setDataSource(mockDataSource);
    }
    catch (Exception e) {
      fail("Setting the dataSource property to a non-null DataSource threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertSame(mockDataSource, delegatingDataSource.getDataSource());
  }

  public void testSetDataSourceThrowsException() throws Exception {
    final AbstractDelegatingDataSource delegatingDataSource = getDelegatingDataSource(null);

    try {
      delegatingDataSource.setDataSource(null);
      fail("Setting the dataSource property to null should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The data source to delegate JDBC operations to cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the dataSource property to null threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  protected static final class MockDelegatingDataSource extends AbstractDelegatingDataSource {

    private final DataSource dataSource;

    public MockDelegatingDataSource(final DataSource dataSource) {
      this.dataSource = dataSource;
    }

    @Override
    protected DataSource getDataSource() {
      if (ObjectUtil.isNotNull(dataSource)) {
        return dataSource;
      }
      return super.getDataSource();
    }

    protected DataSource getDataSource(final DataSourceDefinition dataSourceDefinition) {
      return dataSource;
    }
  }

}
