
package com.cp.tools.sqlloader.validation.support;

import com.cp.common.lang.Assert;
import com.cp.common.lang.RelationalOperator;
import com.cp.tools.sqlloader.validation.AbstractBoundedConstraint;

public class BoundedStringLengthConstraint extends AbstractBoundedConstraint<Integer, String> {

  public BoundedStringLengthConstraint() {
    this(0, Integer.MAX_VALUE);
  }

  public BoundedStringLengthConstraint(final int minLength, final int maxLength) {
    Assert.greaterThanEqual(minLength, 0, "The minimum length of a String value must be greater than or equal to 0!");
    Assert.greaterThanEqual(maxLength, 0, "The maximum length of a String value must be greater than or equal to 0!");
    Assert.greaterThanEqual(maxLength, minLength, "The max length of a String value must be greater than it's min length!");
    setLowerBound(minLength);
    setUpperBound(maxLength);
  }

  public static BoundedStringLengthConstraint getLowerBoundedConstraint(final int minLength) {
    return new BoundedStringLengthConstraint(minLength, Integer.MAX_VALUE);
  }

  public static BoundedStringLengthConstraint getLowerAndUpperBoundedConstraint(final int minLength, final int maxLength) {
    return new BoundedStringLengthConstraint(minLength, maxLength);
  }

  public static BoundedStringLengthConstraint getUpperBoundedConstraint(final int maxLength) {
    return new BoundedStringLengthConstraint(0, maxLength);
  }

  public boolean accept(final String value) {
    return RelationalOperator.getGreaterThanEqualToAndLessThanEqualTo(getLowerBound(), getUpperBound()).accept(length(value));
  }

}
