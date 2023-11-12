
package com.cp.tools.sqlloader.validation.support;

import com.cp.tools.sqlloader.validation.Constraint;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UniqueConstraintTest extends TestCase {

  public UniqueConstraintTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(UniqueConstraintTest.class);
    return suite;
  }

  protected <T> UniqueConstraint<T> getUniqueConstraint() {
    return new UniqueConstraint<T>();
  }

  public void testAccept() throws Exception {
    final Constraint<Object> uniqueConstraint = getUniqueConstraint();

    assertTrue(uniqueConstraint.accept(1));
    assertTrue(uniqueConstraint.accept("1"));
    assertTrue(uniqueConstraint.accept("one"));
    assertTrue(uniqueConstraint.accept(-1));
    assertTrue(uniqueConstraint.accept("-1"));
    assertTrue(uniqueConstraint.accept("minus one"));
    assertFalse(uniqueConstraint.accept(1));
    assertFalse(uniqueConstraint.accept(01));
    assertTrue(uniqueConstraint.accept(10));
  }

}
