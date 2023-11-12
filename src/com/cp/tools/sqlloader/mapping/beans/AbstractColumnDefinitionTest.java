
package com.cp.tools.sqlloader.mapping.beans;

import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Mockery;

public class AbstractColumnDefinitionTest extends TestCase {

  private final Mockery context = new Mockery();

  public AbstractColumnDefinitionTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractColumnDefinitionTest.class);
    return suite;
  }

  protected ColumnDefinition getColumnDefinition(final String name) {
    return new DefaultColumnDefinition(name);
  }

  public void testInstantiate() throws Exception {
    ColumnDefinition columnDefinition = null;

    assertNull(columnDefinition);

    try {
      columnDefinition = getColumnDefinition("myColumn");
    }
    catch (Exception e) {
      fail("Instantiating an instance of ColumnDefinition with a non-null, non-empty column name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(columnDefinition);
    assertEquals("myColumn", columnDefinition.getName());
  }

  public void testInstantiateWithEmptyColumnName() throws Exception {
    ColumnDefinition columnDefinition = null;

    assertNull(columnDefinition);

    try {
      columnDefinition = getColumnDefinition(" ");
      fail("Instanting an instance of ColumnDefinition with an empty column name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the column cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of ColumnDefinition with an empty column name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(columnDefinition);
  }

  public void testInstantiateWithNullColumnName() throws Exception {
    ColumnDefinition columnDefinition = null;

    assertNull(columnDefinition);

    try {
      columnDefinition = getColumnDefinition(null);
      fail("Instanting an instance of ColumnDefinition with a null column name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the column cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of ColumnDefinition with a null column name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(columnDefinition);
  }

  public void testAddConstraintDefinition() throws Exception {
    final ColumnDefinition columnDefinition = getColumnDefinition("myColumn");

    assertNotNull(columnDefinition);
    assertEquals("myColumn", columnDefinition.getName());
    assertFalse(columnDefinition.isConstrained());
    assertEquals(0, columnDefinition.getConstraintDefinitionCount());

    final ConstraintDefinition mockConstraint = context.mock(ConstraintDefinition.class);

    assertNotNull(mockConstraint);

    try {
      assertTrue(columnDefinition.add(mockConstraint));
    }
    catch (Exception e) {
      fail("Adding a non-null ConstraintDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertTrue(columnDefinition.isConstrained());
    assertEquals(1, columnDefinition.getConstraintDefinitionCount());
  }

  public void testAddConstraintDefinitionThrowsNullPointerException() throws Exception {
    final ColumnDefinition columnDefinition = getColumnDefinition("myColumn");

    assertNotNull(columnDefinition);
    assertEquals("myColumn", columnDefinition.getName());
    assertFalse(columnDefinition.isConstrained());
    assertEquals(0, columnDefinition.getConstraintDefinitionCount());

    try {
      columnDefinition.add(null);
      fail("Calling add with a null ConstraintDefinition should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The constraint entry cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling add with a null ConstraintDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertFalse(columnDefinition.isConstrained());
    assertEquals(0, columnDefinition.getConstraintDefinitionCount());
  }

  public void testSetType() throws Exception {
    final ColumnDefinition columnDefinition = getColumnDefinition("myColumn");

    assertNotNull(columnDefinition);
    assertEquals("myColumn", columnDefinition.getName());
    assertNull(columnDefinition.getType());

    try {
      columnDefinition.setType(String.class);
    }
    catch (Exception e) {
      fail("Calling setType with the String class threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(String.class, columnDefinition.getType());

    try {
      columnDefinition.setType("java.util.Calendar");
    }
    catch (Exception e) {
      fail("Calling setType with the fully-qualified class name of Calendar threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(Calendar.class, columnDefinition.getType());
  }

  public void testSetTypeThrowsClassNotFoundException() throws Exception {
    final ColumnDefinition columnDefinition = getColumnDefinition("myColumn");

    assertNotNull(columnDefinition);
    assertEquals("myColumn", columnDefinition.getName());
    assertNull(columnDefinition.getType());

    try {
      columnDefinition.setType("com.mycompany.mypackage.MyType");
      fail("Calling setType with a non-existent fully-qualified class name should have thrown a ClassNotFoundException!");
    }
    catch (ClassNotFoundException e) {
      // expected behavior!
    }
    catch (Exception e) {
      fail("Calling setType with a non-existent fully-qualified class name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(columnDefinition.getType());
  }

}
