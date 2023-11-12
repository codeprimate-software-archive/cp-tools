
package com.cp.tools.sqlloader.mapping.beans;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MappingBeanFactoryTest extends TestCase {

  public MappingBeanFactoryTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(MappingBeanFactoryTest.class);
    return suite;
  }

  public void testGetColumnDefinition() throws Exception {
    final ColumnDefinition columnDefinition = MappingBeanFactory.getColumnDefinition("myColumn");

    assertNotNull(columnDefinition);
    assertEquals("myColumn", columnDefinition.getName());
    assertEquals(0, columnDefinition.getConstraintDefinitionCount());
    assertTrue(columnDefinition.getConstraintDefinitions().isEmpty());
    assertEquals(-1, columnDefinition.getEndIndex());
    assertNull(columnDefinition.getProperty());
    assertEquals(-1, columnDefinition.getStartIndex());
    assertNull(columnDefinition.getType());
  }

  public void testGetConstraintDefinition() throws Exception {
    final ConstraintDefinition constraintDefinition = MappingBeanFactory.getConstraintDefinition("myConstraint");

    assertNotNull(constraintDefinition);
    assertEquals("myConstraint", constraintDefinition.getName());
    assertNull(constraintDefinition.getMax());
    assertNull(constraintDefinition.getMin());
    assertNull(constraintDefinition.getPattern());
  }

  public void testGetDataSourceDefinition() throws Exception {
    final DataSourceDefinition dataSourceDefinition = MappingBeanFactory.getDataSourceDefinition("myDataSource");

    assertNotNull(dataSourceDefinition);
    assertEquals("myDataSource", dataSourceDefinition.getName());
    assertNull(dataSourceDefinition.getJdbcDriver());
    assertNull(dataSourceDefinition.getJdbcPassword());
    assertNull(dataSourceDefinition.getJdbcUrl());
    assertNull(dataSourceDefinition.getJdbcUsername());
    assertTrue(dataSourceDefinition.getTableDefinitions().isEmpty());
  }

  public void testGetTableDefinition() throws Exception {
    final TableDefinition tableDefinition = MappingBeanFactory.getTableDefinition("myTable");

    assertNotNull(tableDefinition);
    assertEquals("myTable", tableDefinition.getName());
    assertEquals(0, tableDefinition.getColumnDefinitionCount());
    assertTrue(tableDefinition.getColumnDefinitions().isEmpty());
    assertNull(tableDefinition.getSource());
  }

}
