
package com.cp.tools.sqlloader.validation;

public final class DefaultConstraint implements Constraint<Object> {

  public static final DefaultConstraint INSTANCE = new DefaultConstraint();

  private DefaultConstraint() {
  }

  public boolean accept(final Object value) {
    return true;
  }

  public void validate(final Object value) throws ConstraintValidationException {
    // no op
  }

}
