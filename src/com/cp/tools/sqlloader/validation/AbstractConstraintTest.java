
package com.cp.tools.sqlloader.validation;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractConstraintTest extends TestCase {

  public AbstractConstraintTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractConstraintTest.class);
    return suite;
  }

  public void testValidate() throws Exception {
    try {
      new MockConstraint().validate("test");
      fail("Calling the validate method of the MockConstraint should have thrown a ConstraintValidationException!");
    }
    catch (ConstraintValidationException e) {
      assertEquals("Constraint validation failed!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling the validate method of MockConstraint threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  private static final class MockConstraint extends AbstractConstraint<Object> {

    public boolean accept(final Object value) {
      return false;
    }
  }

}
