
package com.cp.tools.sqlloader.validation.support;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RegexConstraintTest extends TestCase {

  public RegexConstraintTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(RegexConstraintTest.class);
    return suite;
  }

  public void testAccept() throws Exception {
    final RegexConstraint constraint = new RegexConstraint("%[a-zA-Z0-9]*\\*");

    assertTrue(constraint.accept("%abc123*"));
    assertTrue(constraint.accept("%ABC123*"));
    assertTrue(constraint.accept("%aAbBcC123*"));
    assertFalse(constraint.accept("test%aAbBcC123* @$$"));
    assertFalse(constraint.accept("test%aAbBcC123@$$"));
    assertFalse(constraint.accept("%aAbBcC$123*"));
  }

}
