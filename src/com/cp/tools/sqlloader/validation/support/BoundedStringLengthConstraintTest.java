
package com.cp.tools.sqlloader.validation.support;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BoundedStringLengthConstraintTest extends TestCase {

  public BoundedStringLengthConstraintTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BoundedStringLengthConstraintTest.class);
    return suite;
  }

  public void testInstantiation() throws Exception {
    BoundedStringLengthConstraint constraint = null;

    try {
      constraint = new BoundedStringLengthConstraint();
    }
    catch (Exception e) {
      fail("Constructing an instance of the BoundedLengthConstraint using the default constructor should not have thrown an Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(constraint);
    constraint = null;
    assertNull(constraint);

    try {
      constraint = new BoundedStringLengthConstraint(1, 9);
    }
    catch (Exception e) {
      fail("Constructing an instance of the BoundedLengthConstraint with a min length of 1 and max length of 9 should not have thrown an Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(constraint);
  }

  public void testInstantiationThrowsException() throws Exception {
    BoundedStringLengthConstraint constraint = null;

    try {
      constraint = new BoundedStringLengthConstraint(-1, 10);
      fail("Constructing an instance of the BoundedLengthConstraint with an illegal minimum length should have thrown an IllegalArgumentExcweption!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The minimum length of a String value must be greater than or equal to 0!", e.getMessage());
    }
    catch (Exception e) {
      fail("Constructing an instance of the BoundedLengthConstraint with an illegal minimum length threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(constraint);

    try {
      constraint = new BoundedStringLengthConstraint(1, -1);
      fail("Constructing an instance of the BoundedLengthConstraint with an illegal maximum length should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The maximum length of a String value must be greater than or equal to 0!", e.getMessage());
    }
    catch (Exception e) {
      fail("Constructing an instance of the BoundedLengthConstraint with an illegal maximum length threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(constraint);

    try {
      constraint = new BoundedStringLengthConstraint(10, 5);
      fail("Constructing an instance of the BoundedLengthConstraint with an illegal minimum and maximum length should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The max length of a String value must be greater than it's min length!", e.getMessage());
    }
    catch (Exception e) {
      fail("Constructing an instance of the BoundedLengthConstraint with an illegal minimum and maximum length threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(constraint);
  }

  public void testAcceptMinLength() throws Exception {
    assertTrue(BoundedStringLengthConstraint.getLowerBoundedConstraint(2).accept("12345"));
    assertTrue(BoundedStringLengthConstraint.getLowerBoundedConstraint(2).accept("12"));
    assertFalse(BoundedStringLengthConstraint.getLowerBoundedConstraint(2).accept("2"));
    assertTrue(BoundedStringLengthConstraint.getLowerBoundedConstraint(2).accept("-1"));
    assertTrue(BoundedStringLengthConstraint.getLowerBoundedConstraint(2).accept("01"));
    assertTrue(BoundedStringLengthConstraint.getLowerBoundedConstraint(2).accept("_1"));
    assertTrue(BoundedStringLengthConstraint.getLowerBoundedConstraint(2).accept(" 1"));
  }

  public void testAcceptMaxLength() throws Exception {
    assertTrue(BoundedStringLengthConstraint.getUpperBoundedConstraint(5).accept("123"));
    assertTrue(BoundedStringLengthConstraint.getUpperBoundedConstraint(5).accept("12345"));
    assertFalse(BoundedStringLengthConstraint.getUpperBoundedConstraint(5).accept(" 1 2 3 "));
    assertTrue(BoundedStringLengthConstraint.getUpperBoundedConstraint(5).accept("2 4 8"));
  }

  public void testAcceptMinMaxLength() throws Exception {
    assertTrue(BoundedStringLengthConstraint.getLowerAndUpperBoundedConstraint(2, 5).accept("123"));
    assertTrue(BoundedStringLengthConstraint.getLowerAndUpperBoundedConstraint(2, 5).accept("12"));
    assertTrue(BoundedStringLengthConstraint.getLowerAndUpperBoundedConstraint(2, 5).accept("12345"));
    assertFalse(BoundedStringLengthConstraint.getLowerAndUpperBoundedConstraint(2, 5).accept("1"));
    assertFalse(BoundedStringLengthConstraint.getLowerAndUpperBoundedConstraint(2, 5).accept("123456"));
  }

}
