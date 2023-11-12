
package com.cp.tools.sqlloader.validation.support;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BoundedNumericValueConstraintTest extends TestCase {

  public BoundedNumericValueConstraintTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BoundedNumericValueConstraintTest.class);
    return suite;
  }

  public void testInstantiate() throws Exception {
    BoundedNumericValueConstraint constraint = null;

    try {
      constraint = new BoundedNumericValueConstraint();
    }
    catch (Exception e) {
      fail("Constructing an instance of the BoundedNumericValueConstraint using the default constructor should not have thrown an Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(constraint);
    constraint = null;
    assertNull(constraint);

    try {
      constraint = new BoundedNumericValueConstraint(0, 9);
    }
    catch (Exception e) {
      fail("Constructing an instance of the BoundedNumericValueConstraint with an min and max value should not have thrown an Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(constraint);
  }

  public void testInstantiateThrowsException() throws Exception {
    BoundedNumericValueConstraint constraint = null;

    try {
      constraint = new BoundedNumericValueConstraint(1, -10);
      fail("Constructing an instance of the BoundedNumericValueConstraint with an illegal min and max value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The maximum value must be greater than or equal to the minimum value!", e.getMessage());
    }
    catch (Exception e) {
      fail("Constructing an instance of the BoundedNumericValueConstraint with an illegal min and max threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(constraint);
  }

  public void testAcceptMinValue() throws Exception {
    assertTrue(BoundedNumericValueConstraint.getLowerBoundedConstraint(0).accept(1));
    assertTrue(BoundedNumericValueConstraint.getLowerBoundedConstraint(0).accept(0));
    assertFalse(BoundedNumericValueConstraint.getLowerBoundedConstraint(0).accept(-1));
  }

  public void testAcceptMaxValue() throws Exception {
    assertFalse(BoundedNumericValueConstraint.getUpperBoundedConstraint(0).accept(1));
    assertTrue(BoundedNumericValueConstraint.getUpperBoundedConstraint(0).accept(0));
    assertTrue(BoundedNumericValueConstraint.getUpperBoundedConstraint(0).accept(-1));
  }

  public void testAcceptMinMaxValue() throws Exception {
    assertFalse(BoundedNumericValueConstraint.getLowerAndUpperBoundedConstraint(0, 2).accept(-1));
    assertTrue(BoundedNumericValueConstraint.getLowerAndUpperBoundedConstraint(0, 2).accept(0));
    assertTrue(BoundedNumericValueConstraint.getLowerAndUpperBoundedConstraint(0, 2).accept(1));
    assertTrue(BoundedNumericValueConstraint.getLowerAndUpperBoundedConstraint(0, 2).accept(2));
    assertFalse(BoundedNumericValueConstraint.getLowerAndUpperBoundedConstraint(0, 2).accept(3));
  }

}
