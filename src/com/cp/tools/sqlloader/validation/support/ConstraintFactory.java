
package com.cp.tools.sqlloader.validation.support;

import com.cp.tools.sqlloader.validation.Constraint;
import com.cp.tools.sqlloader.validation.DefaultConstraint;
import java.util.Calendar;

public class ConstraintFactory {

  protected ConstraintFactory() {
  }

  public static Constraint getAlhpaNumericConstraint() {
    return AlphaNumericConstraint.INSTANCE;
  }

  public static Constraint getBoundedNumericValueConstraint(final int minValue, final int maxValue) {
    return new BoundedNumericValueConstraint(minValue, maxValue);
  }

  public static Constraint getBoundedStringLengthConstraint(final int minLength, final int maxLength) {
    return new BoundedStringLengthConstraint(minLength, maxLength);
  }

  public static Constraint getBoundedTimeConstraint(final Calendar minDate, final Calendar maxDate) {
    return new BoundedTimeConstraint(minDate, maxDate);
  }

  public static Constraint getDefaultConstraint() {
    return DefaultConstraint.INSTANCE;
  }

  public static Constraint getDigitsOnlyConstraint() {
    return DigitsOnlyConstraint.INSTANCE;
  }

  public static Constraint getLettersOnlyConstraint() {
    return LettersOnlyConstraint.INSTANCE;
  }

  public static Constraint getNotEmptyConstraint() {
    return NotEmptyConstraint.INSTANCE;
  }

  public static Constraint getNotNullConstraint() {
    return NotNullConstraint.INSTANCE;
  }

  public static Constraint getRegexConstraint(final String regularExpression) {
    return new RegexConstraint(regularExpression);
  }

  public static <T> Constraint<T> getUniqueConstraint() {
    return new UniqueConstraint<T>();
  }

}
