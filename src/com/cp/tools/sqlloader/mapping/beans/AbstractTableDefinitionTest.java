
package com.cp.tools.sqlloader.mapping.beans;

import java.util.LinkedList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class AbstractTableDefinitionTest extends TestCase {

  private final Mockery context = new Mockery();

  public AbstractTableDefinitionTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractTableDefinitionTest.class);
    return suite;
  }

  protected TableDefinition getTableDefinition(final String name) {
    return new DefaultTableDefinition(name);
  }

  public void testInstantiate() throws Exception {
    TableDefinition tableDefinition = null;

    assertNull(tableDefinition);

    try {
      tableDefinition = getTableDefinition("myTable");
    }
    catch (Exception e) {
      fail("Instantiating an instance of TableDefinition with a non-null, non-empty table name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(tableDefinition);
    assertEquals("myTable", tableDefinition.getName());
  }

  public void testInstantiateWithEmptyTableName() throws Exception {
    TableDefinition tableDefinition = null;

    assertNull(tableDefinition);

    try {
      tableDefinition = getTableDefinition(" ");
      fail("Instantiating an instance of TableDefinition with an empty table name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the table cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of TableDefinition with an empty table name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(tableDefinition);
  }

  public void testInstantiateWithNullTableName() throws Exception {
    TableDefinition tableDefinition = null;

    assertNull(tableDefinition);

    try {
      tableDefinition = getTableDefinition(null);
      fail("Instantiating an instance of TableDefinition with a null table name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the table cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of TableDefinition with a null table name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(tableDefinition);
  }

  public void testAddColumnDefinition() throws Exception {
    final TableDefinition tableDefinition = getTableDefinition("myTable");

    assertNotNull(tableDefinition);
    assertEquals(0, tableDefinition.getColumnDefinitionCount());

    final ColumnDefinition mockColumnDefinition = context.mock(ColumnDefinition.class);

    try {
      assertTrue(tableDefinition.add(mockColumnDefinition));
    }
    catch (Exception e) {
      fail("Adding a non-null ColumnDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(1, tableDefinition.getColumnDefinitionCount());
  }

  public void testAddColumnDefinitionThrowsNullPointerException() throws Exception {
    final TableDefinition tableDefinition = getTableDefinition("myTable");

    assertNotNull(tableDefinition);
    assertEquals(0, tableDefinition.getColumnDefinitionCount());

    try {
      tableDefinition.add(null);
      fail("Adding a null ColumnDefinition should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The column entry cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Adding a null ColumnDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(0, tableDefinition.getColumnDefinitionCount());
  }

  public void testGetColumnDefinitionAtIndex() throws Exception {
    final TableDefinition tableDefinition = getTableDefinition("myTable");

    assertNotNull(tableDefinition);
    assertEquals(0, tableDefinition.getColumnDefinitionCount());

    final ColumnDefinition personIdColumn = context.mock(ColumnDefinition.class, "personId");
    final ColumnDefinition firstNameColumn = context.mock(ColumnDefinition.class, "firstName");
    final ColumnDefinition lastNameColumn = context.mock(ColumnDefinition.class, "lastName");
    final ColumnDefinition dateOfBirthColumn = context.mock(ColumnDefinition.class, "dateOfBirth");

    try {
      assertTrue(tableDefinition.add(personIdColumn));
      assertTrue(tableDefinition.add(firstNameColumn));
      assertTrue(tableDefinition.add(lastNameColumn));
      assertTrue(tableDefinition.add(dateOfBirthColumn));
    }
    catch (Exception e) {
      fail("Adding 4 ColumnDefinitions (personId, firstName, lastName, dateOfBirth) threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(4, tableDefinition.getColumnDefinitionCount());
    assertSame(personIdColumn, tableDefinition.getColumnDefinitionAtIndex(0));
    assertSame(firstNameColumn, tableDefinition.getColumnDefinitionAtIndex(1));
    assertSame(lastNameColumn, tableDefinition.getColumnDefinitionAtIndex(2));
    assertSame(dateOfBirthColumn, tableDefinition.getColumnDefinitionAtIndex(3));
  }

  public void testGetColumnDefinitionAtIndexThrowsIndexOutOfBoundsException() throws Exception {
    final TableDefinition tableDefinition = getTableDefinition("myTable");

    assertNotNull(tableDefinition);
    assertEquals(0, tableDefinition.getColumnDefinitionCount());

    final ColumnDefinition ssnColumn = context.mock(ColumnDefinition.class, "ssn");

    try {
      assertTrue(tableDefinition.add(ssnColumn));
    }
    catch (Exception e) {
      fail("Adding a ColumnDefinition (ssn) threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(1, tableDefinition.getColumnDefinitionCount());
    assertSame(ssnColumn, tableDefinition.getColumnDefinitionAtIndex(0));

    try {
      tableDefinition.getColumnDefinitionAtIndex(-1);
      fail("Calling getColumnDefinitionAtIndex with a -1 should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }
    catch (Exception e) {
      fail("Calling getColumnDefinitionAtIndex with a -1 threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    try {
      tableDefinition.getColumnDefinitionAtIndex(1);
      fail("Calling getColumnDefinitionAtIndex with 1 should have thrown an IndexOutOfBoundsException!");
    }
    catch (IndexOutOfBoundsException e) {
      // expected behavior!
    }
    catch (Exception e) {
      fail("Calling getColumnDefinitionAtIndex with 1 threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testGetColumnDefinitionByName() throws Exception {
    final TableDefinition tableDefinition = getTableDefinition("myTable");

    assertNotNull(tableDefinition);
    assertEquals(0, tableDefinition.getColumnDefinitionCount());

    final ColumnDefinition addressIdColumn = context.mock(ColumnDefinition.class, "addressId");
    final ColumnDefinition streetColumn = context.mock(ColumnDefinition.class, "street");
    final ColumnDefinition cityColumn = context.mock(ColumnDefinition.class, "city");
    final ColumnDefinition stateColumn = context.mock(ColumnDefinition.class, "state");
    final ColumnDefinition zipColumn = context.mock(ColumnDefinition.class, "zip");

    context.checking(new Expectations() {{
      allowing(addressIdColumn).getName();
      will(returnValue("addressId"));
      allowing(streetColumn).getName();
      will(returnValue("street"));
      allowing(cityColumn).getName();
      will(returnValue("city"));
      allowing(stateColumn).getName();
      will(returnValue("state"));
      allowing(zipColumn).getName();
      will(returnValue("zip"));
    }});

    try {
      assertTrue(tableDefinition.add(addressIdColumn));
      assertTrue(tableDefinition.add(streetColumn));
      assertTrue(tableDefinition.add(cityColumn));
      assertTrue(tableDefinition.add(stateColumn));
      assertTrue(tableDefinition.add(zipColumn));
    }
    catch (Exception e) {
      fail("Adding 4 ColumnDefinitions (personId, firstName, lastName, dateOfBirth) threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(5, tableDefinition.getColumnDefinitionCount());
    assertSame(addressIdColumn, tableDefinition.getColumnDefinitionByName("addressId"));
    assertNull(tableDefinition.getColumnDefinitionByName("AddressId"));
    assertNull(tableDefinition.getColumnDefinitionByName("ADDRESSID"));
    assertNull(tableDefinition.getColumnDefinitionByName("ID"));
    assertSame(streetColumn, tableDefinition.getColumnDefinitionByName("street"));
    assertNull(tableDefinition.getColumnDefinitionByName("st."));
    assertSame(stateColumn, tableDefinition.getColumnDefinitionByName("state"));
    assertNull(tableDefinition.getColumnDefinitionByName("ST"));
    assertSame(zipColumn, tableDefinition.getColumnDefinitionByName("zip"));
    assertNull(tableDefinition.getColumnDefinitionByName("zipCode"));
    assertNull(tableDefinition.getColumnDefinitionByName(null));
    assertNull(tableDefinition.getColumnDefinitionByName("personId"));
    assertNull(tableDefinition.getColumnDefinitionByName("ssn"));

    context.assertIsSatisfied();
  }

  public void testGetColumnDefinitions() throws Exception {
    final TableDefinition tableDefinition = getTableDefinition("myTable");

    assertNotNull(tableDefinition);
    assertEquals(0, tableDefinition.getColumnDefinitionCount());

    final List<ColumnDefinition> expectedColumnDefinitions = new LinkedList<ColumnDefinition>();
    expectedColumnDefinitions.add(context.mock(ColumnDefinition.class, "column1"));
    expectedColumnDefinitions.add(context.mock(ColumnDefinition.class, "column2"));
    expectedColumnDefinitions.add(context.mock(ColumnDefinition.class, "column3"));

    assertEquals(3, expectedColumnDefinitions.size());

    try {
      for (final ColumnDefinition columnDefinition : expectedColumnDefinitions) {
        assertTrue(tableDefinition.add(columnDefinition));
      }
    }
    catch (Exception e) {
      fail("Adding 3 ColumnDefinitions (column1, column2, column3) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(expectedColumnDefinitions.size(), tableDefinition.getColumnDefinitionCount());

    final List<ColumnDefinition> actualColumnDefinitions = tableDefinition.getColumnDefinitions();

    assertNotNull(actualColumnDefinitions);
    assertEquals(expectedColumnDefinitions, actualColumnDefinitions);
  }

}
