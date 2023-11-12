
package com.cp.tools.sqlloader.validation;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractBoundedConstraintTest extends TestCase {

  public AbstractBoundedConstraintTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractBoundedConstraintTest.class);
    return suite;
  }

  public void testSetLowerBound() throws Exception {
    final MockBoundedConstraint constraint = new MockBoundedConstraint();

    assertNull(constraint.getLowerBound());

    constraint.setLowerBound(2);

    assertEquals(new Integer(2), constraint.getLowerBound());
  }

  public void testSetUpperBound() throws Exception {
    final MockBoundedConstraint constraint = new MockBoundedConstraint();

    assertNull(constraint.getUpperBound());

    constraint.setUpperBound(9);

    assertEquals(new Integer(9), constraint.getUpperBound());
  }


  private static final class MockBoundedConstraint extends AbstractBoundedConstraint<Integer, String> {

    public boolean accept(final String value) {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

}
