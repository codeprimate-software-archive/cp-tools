
package com.cp.tools.sqlloader.validation.support;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LettersOnlyConstraintTest extends TestCase {

  public LettersOnlyConstraintTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(LettersOnlyConstraintTest.class);
    return suite;
  }

  public void testAccept() throws Exception {
    assertTrue(LettersOnlyConstraint.INSTANCE.accept("abcdefghijklmnopqrstuvwxyz"));
    assertTrue(LettersOnlyConstraint.INSTANCE.accept("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    assertTrue(LettersOnlyConstraint.INSTANCE.accept("abcDEFghiJKLmnoPQRstuVWXyz"));
    assertFalse(LettersOnlyConstraint.INSTANCE.accept("abc123"));
    assertFalse(LettersOnlyConstraint.INSTANCE.accept("1053R"));
  }

}
