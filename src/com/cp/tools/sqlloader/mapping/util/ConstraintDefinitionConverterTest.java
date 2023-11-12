
package com.cp.tools.sqlloader.mapping.util;

import com.cp.tools.sqlloader.mapping.beans.ConstraintDefinition;
import com.cp.tools.sqlloader.validation.Constraint;
import com.cp.tools.sqlloader.validation.DefaultConstraint;
import com.cp.tools.sqlloader.validation.support.AlphaNumericConstraint;
import com.cp.tools.sqlloader.validation.support.BoundedNumericValueConstraint;
import com.cp.tools.sqlloader.validation.support.BoundedStringLengthConstraint;
import com.cp.tools.sqlloader.validation.support.BoundedTimeConstraint;
import com.cp.tools.sqlloader.validation.support.DigitsOnlyConstraint;
import com.cp.tools.sqlloader.validation.support.LettersOnlyConstraint;
import com.cp.tools.sqlloader.validation.support.NotEmptyConstraint;
import com.cp.tools.sqlloader.validation.support.NotNullConstraint;
import com.cp.tools.sqlloader.validation.support.RegexConstraint;
import com.cp.tools.sqlloader.validation.support.UniqueConstraint;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class ConstraintDefinitionConverterTest extends TestCase {

  private final Mockery context = new Mockery();

  public ConstraintDefinitionConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ConstraintDefinitionConverterTest.class);
    return suite;
  }

  protected void assertSameConstraint(final ConstraintType constraintType, final Constraint expectedConstraint) throws Exception {
    final ConstraintDefinition mockConstraintDefinition = context.mock(ConstraintDefinition.class, constraintType.getName());

    context.checking(new Expectations() {{
      exactly(2).of(mockConstraintDefinition).getName();
      will(returnValue(constraintType.getName()));
    }});

    final Constraint actualConstraint = ConstraintDefinitionConverter.toConstraint(mockConstraintDefinition);

    assertNotNull(actualConstraint);
    assertSame(expectedConstraint, actualConstraint);

    context.assertIsSatisfied();
  }

  public void testToBoundedLengthConstraint() throws Exception {
    final ConstraintDefinition mockConstraintDefinition = context.mock(ConstraintDefinition.class, ConstraintType.BOUNDED_LENGTH.getName());

    context.checking(new Expectations() {{
      exactly(2).of(mockConstraintDefinition).getName();
      will(returnValue(ConstraintType.BOUNDED_LENGTH.getName()));
      one(mockConstraintDefinition).getMax();
      will(returnValue("8"));
      one(mockConstraintDefinition).getMin();
      will(returnValue("2"));
    }});

    final Constraint constraint = ConstraintDefinitionConverter.toConstraint(mockConstraintDefinition);

    assertTrue(constraint instanceof BoundedStringLengthConstraint);

    final BoundedStringLengthConstraint boundedLengthConstraint = (BoundedStringLengthConstraint) constraint;

    assertEquals(new Integer(2), boundedLengthConstraint.getLowerBound());
    assertEquals(new Integer(8), boundedLengthConstraint.getUpperBound());

    context.assertIsSatisfied();
  }

  public void testToBoundedNumericConstraint() throws Exception {
    final ConstraintDefinition mockConstraintDefinition = context.mock(ConstraintDefinition.class, ConstraintType.BOUNDED_NUMERIC.getName());

    context.checking(new Expectations() {{
      exactly(2).of(mockConstraintDefinition).getName();
      will(returnValue(ConstraintType.BOUNDED_NUMERIC.getName()));
      one(mockConstraintDefinition).getMax();
      will(returnValue("9"));
      one(mockConstraintDefinition).getMin();
      will(returnValue("-9"));
    }});

    final Constraint constraint = ConstraintDefinitionConverter.toConstraint(mockConstraintDefinition);

    assertTrue(constraint instanceof BoundedNumericValueConstraint);

    final BoundedNumericValueConstraint boundedNumericValueConstraint = (BoundedNumericValueConstraint) constraint;

    assertEquals(new Integer(-9), boundedNumericValueConstraint.getLowerBound());
    assertEquals(new Integer(9), boundedNumericValueConstraint.getUpperBound());

    context.assertIsSatisfied();
  }

  public void testToBoundedTimeConstraint() throws Exception {
    final ConstraintDefinition mockConstraintDefinition = context.mock(ConstraintDefinition.class, ConstraintType.BOUNDED_TIME.getName());

    context.checking(new Expectations() {{
      exactly(2).of(mockConstraintDefinition).getName();
      will(returnValue(ConstraintType.BOUNDED_TIME.getName()));
      one(mockConstraintDefinition).getMax();
      will(returnValue("12/31/2008"));
      one(mockConstraintDefinition).getMin();
      will(returnValue("01/01/2008"));
      one(mockConstraintDefinition).getPattern();
      will(returnValue("MM/dd/yyyy"));
    }});

    final Constraint constraint = ConstraintDefinitionConverter.toConstraint(mockConstraintDefinition);

    assertTrue(constraint instanceof BoundedTimeConstraint);

    final BoundedTimeConstraint boundedNumericValueConstraint = (BoundedTimeConstraint) constraint;

    assertEquals(DateUtil.getCalendar(2008, Calendar.JANUARY, 1), boundedNumericValueConstraint.getLowerBound());
    assertEquals(DateUtil.getCalendar(2008, Calendar.DECEMBER, 31), boundedNumericValueConstraint.getUpperBound());

    context.assertIsSatisfied();
  }

  public void testToDefaultConstraint() throws Exception {
    final ConstraintDefinition mockConstraintDefinition = context.mock(ConstraintDefinition.class, "default");

    context.checking(new Expectations() {{
      exactly(2).of(mockConstraintDefinition).getName();
      will(returnValue("default"));
    }});

    final Constraint constraint = ConstraintDefinitionConverter.toConstraint(mockConstraintDefinition);

    assertTrue(constraint instanceof DefaultConstraint);
    assertSame(DefaultConstraint.INSTANCE, constraint);

    context.assertIsSatisfied();
  }

  public void testToRegexConstraint() throws Exception {
    final ConstraintDefinition mockConstraintDefinition = context.mock(ConstraintDefinition.class, ConstraintType.REGEX.getName());

    context.checking(new Expectations() {{
      exactly(2).of(mockConstraintDefinition).getName();
      will(returnValue(ConstraintType.REGEX.getName()));
      one(mockConstraintDefinition).getPattern();
      will(returnValue("[a-zA-Z0-9]*")); // letters and digits only!
    }});

    final Constraint constraint = ConstraintDefinitionConverter.toConstraint(mockConstraintDefinition);

    assertTrue(constraint instanceof RegexConstraint);

    final RegexConstraint regexConstraint = (RegexConstraint) constraint;

    assertEquals("[a-zA-Z0-9]*", regexConstraint.getRegularExpression());

    context.assertIsSatisfied();
  }

  public void testToUniqueConstraint() throws Exception {
    final ConstraintDefinition mockConstraintDefinition = context.mock(ConstraintDefinition.class, ConstraintType.UNIQUE.getName());

    context.checking(new Expectations() {{
      exactly(2).of(mockConstraintDefinition).getName();
      will(returnValue(ConstraintType.UNIQUE.getName()));
    }});

    final Constraint constraint = ConstraintDefinitionConverter.toConstraint(mockConstraintDefinition);

    assertTrue(constraint instanceof UniqueConstraint);

    context.assertIsSatisfied();
  }

  public void testToConstraintWithInvalidConstraintType() throws Exception {
    Constraint constraint = null;

    assertNull(constraint);

    final ConstraintDefinition mockConstraintDefinition = context.mock(ConstraintDefinition.class, "mockConstraint");

    context.checking(new Expectations() {{
      exactly(2).of(mockConstraintDefinition).getName();
      will(returnValue("mockConstraint"));
    }});

    try {
      constraint = ConstraintDefinitionConverter.toConstraint(mockConstraintDefinition);
      fail("Calling toConstraint with the mock ConstraintDefinition should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("Constraint having name (mockConstraint) is not a valid constraint!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling toConstraint with the mock ConstraintDefinition threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(constraint);

    context.assertIsSatisfied();
  }

  public void testToConstraintWithNull() throws Exception {
    Constraint constraint = null;

    assertNull(constraint);

    try {
      constraint = ConstraintDefinitionConverter.toConstraint(null);
      fail("Calling toConstraint with a null ConstraintDefinition should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The constraint definition cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling toConstraint with a null ConstraintDefinition threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(constraint);
  }

  public void testToSameConstraint() throws Exception {
    assertSameConstraint(ConstraintType.ALPHA_NUMERIC, AlphaNumericConstraint.INSTANCE);
    assertSameConstraint(ConstraintType.DEFAULT, DefaultConstraint.INSTANCE);
    assertSameConstraint(ConstraintType.DIGITS_ONLY, DigitsOnlyConstraint.INSTANCE);
    assertSameConstraint(ConstraintType.LETTERS_ONLY, LettersOnlyConstraint.INSTANCE);
    assertSameConstraint(ConstraintType.NOT_EMPTY, NotEmptyConstraint.INSTANCE);
    assertSameConstraint(ConstraintType.NOT_NULL, NotNullConstraint.INSTANCE);
  }

}
