
package com.cp.tools.sqlloader.validation.support;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AlphaNumericConstraintTest extends TestCase {

  public AlphaNumericConstraintTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AlphaNumericConstraintTest.class);
    return suite;
  }

  public void testAccept() throws Exception {
    assertTrue(AlphaNumericConstraint.INSTANCE.accept("abc"));
    assertTrue(AlphaNumericConstraint.INSTANCE.accept("ABC"));
    assertTrue(AlphaNumericConstraint.INSTANCE.accept("123"));
    assertTrue(AlphaNumericConstraint.INSTANCE.accept("abc123"));
    assertTrue(AlphaNumericConstraint.INSTANCE.accept("00129AbC9210"));
    assertFalse(AlphaNumericConstraint.INSTANCE.accept("@$$!"));
    assertFalse(AlphaNumericConstraint.INSTANCE.accept("@55!"));
    assertFalse(AlphaNumericConstraint.INSTANCE.accept("a$$!"));
  }

}
