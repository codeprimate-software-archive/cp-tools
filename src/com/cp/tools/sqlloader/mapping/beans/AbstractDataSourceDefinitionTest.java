
package com.cp.tools.sqlloader.mapping.beans;

import java.util.LinkedList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Mockery;

public class AbstractDataSourceDefinitionTest extends TestCase {

  private final Mockery context = new Mockery();

  public AbstractDataSourceDefinitionTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractDataSourceDefinitionTest.class);
    return suite;
  }

  protected DataSourceDefinition getDataSourceDefinition(final String name) {
    return new DefaultDataSourceDefinition(name);
  }

  public void testInstantiate() throws Exception {
    DataSourceDefinition dataSourceDefinition = null;

    assertNull(dataSourceDefinition);

    try {
      dataSourceDefinition = getDataSourceDefinition("myDataSource");
    }
    catch (Exception e) {
      fail("Instantiating an instance of DataSourceDefinition with a non-null, non-empty data source name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(dataSourceDefinition);
    assertEquals("myDataSource", dataSourceDefinition.getName());
  }

  public void testInstantiateWithEmptyDataSourceName() throws Exception {
    DataSourceDefinition dataSourceDefinition = null;

    assertNull(dataSourceDefinition);

    try {
      dataSourceDefinition = getDataSourceDefinition(" ");
      fail("Instantiating an instance of DataSourceDefinition with an empty data source name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the data source cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of DataSourceDefinition with an empty data soruce name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(dataSourceDefinition);
  }

  public void testInstantiateWithNullDataSourceName() throws Exception {
    DataSourceDefinition dataSourceDefinition = null;

    assertNull(dataSourceDefinition);

    try {
      dataSourceDefinition = getDataSourceDefinition(null);
      fail("Instantiating an instance of DataSourceDefinition with a null data source name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the data source cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of DataSourceDefinition with a null data soruce name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(dataSourceDefinition);
  }

  public void testAddTableDefinition() throws Exception {
    final DataSourceDefinition dataSourceDefinition = getDataSourceDefinition("myDataSource");

    assertNotNull(dataSourceDefinition);
    assertEquals("myDataSource", dataSourceDefinition.getName());
    assertTrue(dataSourceDefinition.getTableDefinitions().isEmpty());

    final TableDefinition mockTableDefinition = context.mock(TableDefinition.class);

    try {
      assertTrue(dataSourceDefinition.add(mockTableDefinition));
    }
    catch (Exception e) {
      fail("Adding a non-null TableDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertFalse(dataSourceDefinition.getTableDefinitions().isEmpty());
    assertEquals(1, dataSourceDefinition.getTableDefinitions().size());
  }

  public void testAddTableDefinitionThrowsNullPointerException() throws Exception {
    final DataSourceDefinition dataSourceDefinition = getDataSourceDefinition("myDataSource");

    assertNotNull(dataSourceDefinition);
    assertEquals("myDataSource", dataSourceDefinition.getName());
    assertTrue(dataSourceDefinition.getTableDefinitions().isEmpty());

    try {
      dataSourceDefinition.add(null);
      fail("Adding a null TableDefinition should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The table definition cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Adding a null TableDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertTrue(dataSourceDefinition.getTableDefinitions().isEmpty());
  }

  public void testGetTableDefinitions() throws Exception {
    final DataSourceDefinition dataSourceDefinition = getDataSourceDefinition("myDataSource");

    assertNotNull(dataSourceDefinition);
    assertEquals("myDataSource", dataSourceDefinition.getName());
    assertTrue(dataSourceDefinition.getTableDefinitions().isEmpty());

    final List<TableDefinition> expectedTableDefinitions = new LinkedList<TableDefinition>();
    expectedTableDefinitions.add(context.mock(TableDefinition.class, "address"));
    expectedTableDefinitions.add(context.mock(TableDefinition.class, "household"));
    expectedTableDefinitions.add(context.mock(TableDefinition.class, "person"));

    assertFalse(expectedTableDefinitions.isEmpty());
    assertEquals(3, expectedTableDefinitions.size());

    try {
      for (final TableDefinition tableDefinition : expectedTableDefinitions) {
        assertTrue(dataSourceDefinition.add(tableDefinition));
      }
    }
    catch (Exception e) {
      fail("Adding 3 non-null TableDefinitions threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    final List<TableDefinition> actualTableDefinitions = dataSourceDefinition.getTableDefinitions();

    assertNotNull(actualTableDefinitions);
    assertEquals(expectedTableDefinitions, actualTableDefinitions);
  }

}
