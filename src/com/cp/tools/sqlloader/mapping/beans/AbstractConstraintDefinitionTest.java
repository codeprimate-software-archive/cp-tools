
package com.cp.tools.sqlloader.mapping.beans;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractConstraintDefinitionTest extends TestCase {

  public AbstractConstraintDefinitionTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractConstraintDefinitionTest.class);
    return suite;
  }

  protected ConstraintDefinition getConstraintDefinition(final String name) {
    return new DefaultConstraintDefinition(name);
  }

  public void testInstantiate() throws Exception {
    ConstraintDefinition constraintDefinition = null;

    assertNull(constraintDefinition);

    try {
      constraintDefinition = getConstraintDefinition("myConstraint");
    }
    catch (Exception e) {
      fail("Instantiating an instance of ConstraintDefinition with a non-null, non-empty constraint name threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(constraintDefinition);
    assertEquals("myConstraint", constraintDefinition.getName());
  }

  public void testInstantiateWithEmptyConstraintName() throws Exception {
    ConstraintDefinition constraintDefinition = null;

    assertNull(constraintDefinition);

    try {
      constraintDefinition = getConstraintDefinition(" ");
      fail("Instantiating an instance of ConstraintDefinition with an empty constraint name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the constraint cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of ConstraintDefinition with an empty constraint name threw unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(constraintDefinition);
  }

  public void testInstantiateWithNullConstraintName() throws Exception {
    ConstraintDefinition constraintDefinition = null;

    assertNull(constraintDefinition);

    try {
      constraintDefinition = getConstraintDefinition(null);
      fail("Instantiating an instance of ConstraintDefinition with a null constraint name should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the constraint cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of ConstraintDefinition with a null constraint name threw unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(constraintDefinition);
  }

}
