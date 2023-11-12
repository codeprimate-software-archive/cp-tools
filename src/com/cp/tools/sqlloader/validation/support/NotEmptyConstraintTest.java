
package com.cp.tools.sqlloader.validation.support;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NotEmptyConstraintTest extends TestCase {

  public NotEmptyConstraintTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(NotEmptyConstraintTest.class);
    return suite;
  }

  public void testAccept() throws Exception {
    assertTrue(NotEmptyConstraint.INSTANCE.accept("test"));
    assertTrue(NotEmptyConstraint.INSTANCE.accept(" test "));
    assertTrue(NotEmptyConstraint.INSTANCE.accept("___"));
    assertFalse(NotEmptyConstraint.INSTANCE.accept(""));
    assertFalse(NotEmptyConstraint.INSTANCE.accept(" "));
    assertFalse(NotEmptyConstraint.INSTANCE.accept("   "));
  }

}
