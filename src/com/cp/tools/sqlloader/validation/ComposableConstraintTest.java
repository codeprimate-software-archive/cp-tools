
package com.cp.tools.sqlloader.validation;

import com.cp.common.lang.LogicalOperator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ComposableConstraintTest extends TestCase {

  public ComposableConstraintTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ComposableConstraintTest.class);
    return suite;
  }

  protected MockConstraint getConstraint(final Boolean accept) {
    return new MockConstraint(accept);
  }

  public void testCompose() throws Exception {
    final Constraint c0 = getConstraint(true);
    final Constraint c1 = getConstraint(false);

    assertSame(c0, ComposableConstraint.compose(c0, null, LogicalOperator.AND));
    assertSame(c1, ComposableConstraint.compose(null, c1, LogicalOperator.OR));

    final Constraint composedConstraint = ComposableConstraint.compose(c0, c1, LogicalOperator.AND);

    assertNotNull(composedConstraint);
    assertTrue(composedConstraint instanceof ComposableConstraint);
    assertSame(c0, ((ComposableConstraint) composedConstraint).getLeftConstraint());
    assertSame(c1, ((ComposableConstraint) composedConstraint).getRightConstraint());
    assertEquals(LogicalOperator.AND, ((ComposableConstraint) composedConstraint).getOp());

    final Constraint anotherComposedConstraint = ComposableConstraint.compose(c0, composedConstraint, LogicalOperator.OR);

    assertNotNull(anotherComposedConstraint);
    assertTrue(anotherComposedConstraint instanceof ComposableConstraint);
    assertEquals(c0, ((ComposableConstraint) anotherComposedConstraint).getLeftConstraint());
    assertEquals(composedConstraint, ((ComposableConstraint) anotherComposedConstraint).getRightConstraint());
    assertEquals(LogicalOperator.OR, ((ComposableConstraint) anotherComposedConstraint).getOp());
  }

  public void testComposeAnd() throws Exception {
    final MockConstraint c0 = getConstraint(true);
    final MockConstraint c1 = getConstraint(true);
    final MockConstraint c2 = getConstraint(true);
    final MockConstraint c3 = getConstraint(false);

    assertFalse(c0.isCalled());
    assertFalse(c1.isCalled());
    assertFalse(c2.isCalled());
    assertFalse(c3.isCalled());

    Constraint constraint = ComposableConstraint.composeAnd(c0, c1, c2);

    assertNotNull(constraint);
    assertTrue(constraint instanceof ComposableConstraint);
    assertTrue(constraint.accept("test"));
    assertTrue(c0.isCalled());
    assertTrue(c1.isCalled());
    assertTrue(c2.isCalled());
    assertFalse(c3.isCalled());

    constraint = ComposableConstraint.composeAnd(constraint, c3);

    assertNotNull(constraint);
    assertTrue(constraint instanceof ComposableConstraint);
    assertFalse(constraint.accept("test"));
    assertTrue(c0.isCalled());
    assertTrue(c1.isCalled());
    assertTrue(c2.isCalled());
    assertTrue(c3.isCalled());
  }

  public void testComposeOr() throws Exception {
    final MockConstraint c0 = getConstraint(false);
    final MockConstraint c1 = getConstraint(false);
    final MockConstraint c2 = getConstraint(false);
    final MockConstraint c3 = getConstraint(true);

    assertFalse(c0.isCalled());
    assertFalse(c1.isCalled());
    assertFalse(c2.isCalled());
    assertFalse(c3.isCalled());

    Constraint constraint = ComposableConstraint.composeOr(c0, c1, c2);

    assertNotNull(constraint);
    assertTrue(constraint instanceof ComposableConstraint);
    assertFalse(constraint.accept("test"));
    assertTrue(c0.isCalled());
    assertTrue(c1.isCalled());
    assertTrue(c2.isCalled());
    assertFalse(c3.isCalled());

    constraint = ComposableConstraint.composeOr(constraint, c3);

    assertNotNull(constraint);
    assertTrue(constraint instanceof ComposableConstraint);
    assertTrue(constraint.accept("test"));
    assertTrue(c0.isCalled());
    assertTrue(c1.isCalled());
    assertTrue(c2.isCalled());
    assertTrue(c3.isCalled());
  }

  public void testComplexComposition() throws Exception {
    final MockConstraint c0 = new MockConstraint(false);
    final MockConstraint c1 = new MockConstraint(true);
    final MockConstraint c2 = new MockConstraint(true);
    final MockConstraint c3 = new MockConstraint(true);
    final MockConstraint c4 = new MockConstraint(false);

    final Constraint c0ANDc1 = ComposableConstraint.composeAnd(c0, c1);
    final Constraint c3ORc4 = ComposableConstraint.composeOr(c3, c4);
    final Constraint c2ANDc3ORc4 = ComposableConstraint.composeAnd(c2, c3ORc4);
    final Constraint constraint = ComposableConstraint.composeOr(c0ANDc1, c2ANDc3ORc4);

    assertNotNull(constraint);
    assertFalse(c0.isCalled());
    assertFalse(c1.isCalled());
    assertFalse(c2.isCalled());
    assertFalse(c3.isCalled());
    assertFalse(c4.isCalled());
    assertTrue(constraint.accept("test"));
    assertTrue(c0.isCalled());
    assertTrue(c1.isCalled());
    assertTrue(c2.isCalled());
    assertTrue(c3.isCalled());
    assertTrue(c4.isCalled());
  }

  public void testValidate() throws Exception {
    final MockConstraint c0 = new MockConstraint(false);
    final MockConstraint c1 = new MockConstraint(true);

    final Constraint constraint = ComposableConstraint.composeOr(c0, c1);

    assertNotNull(constraint);
    assertTrue(constraint instanceof ComposableConstraint);
    assertFalse(c0.isCalled());
    assertFalse(c1.isCalled());

    try {
      constraint.validate("test");
      fail("Calling validate on the ComposableConstraint should have thrown a ConstraintValidationException!");
    }
    catch (ConstraintValidationException e) {
      assertEquals("Constraint validation failed!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling validate on the ComposableConstraint threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertTrue(c0.isCalled());
    assertFalse(c1.isCalled());
  }

  private static final class MockConstraint extends AbstractConstraint<Object> {

    private final boolean accept;
    private boolean called = false;

    public MockConstraint(final boolean accept) {
      this.accept = accept;
    }

    public boolean isCalled() {
      final boolean calledCopy = this.called;
      this.called = false;
      return calledCopy;
    }

    public boolean accept(final Object value) {
      called = true;
      return accept;
    }
  }

}
