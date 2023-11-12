
package com.cp.tools.sqlloader.validation.support;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DigitsOnlyConstraintTest extends TestCase {

  public DigitsOnlyConstraintTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DigitsOnlyConstraintTest.class);
    return suite;
  }

  public void testAccept() throws Exception {
    assertTrue(DigitsOnlyConstraint.INSTANCE.accept("0123456789"));
    assertTrue(DigitsOnlyConstraint.INSTANCE.accept("10101"));
    assertTrue(DigitsOnlyConstraint.INSTANCE.accept("08"));
    assertFalse(DigitsOnlyConstraint.INSTANCE.accept("-123"));
    assertFalse(DigitsOnlyConstraint.INSTANCE.accept("lO"));
    assertFalse(DigitsOnlyConstraint.INSTANCE.accept("$10.50"));
  }

}
