
package com.cp.tools.sqlloader.validation.support;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NotNullConstraintTest extends TestCase {

  public NotNullConstraintTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(NotNullConstraintTest.class);
    return suite;
  }

  public void testAccept() throws Exception {
    assertTrue(NotNullConstraint.INSTANCE.accept(Boolean.TRUE));
    assertTrue(NotNullConstraint.INSTANCE.accept('A'));
    assertTrue(NotNullConstraint.INSTANCE.accept(2));
    assertTrue(NotNullConstraint.INSTANCE.accept(3.14159));
    assertTrue(NotNullConstraint.INSTANCE.accept("test"));
    assertTrue(NotNullConstraint.INSTANCE.accept("null"));
    assertTrue(NotNullConstraint.INSTANCE.accept("nill"));
    assertFalse(NotNullConstraint.INSTANCE.accept(null));
  }

}
