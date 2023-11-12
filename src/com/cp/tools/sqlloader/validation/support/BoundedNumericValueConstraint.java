
package com.cp.tools.sqlloader.validation.support;

import com.cp.tools.sqlloader.validation.AbstractBoundedConstraint;
import com.cp.common.lang.Assert;
import com.cp.common.lang.RelationalOperator;

public class BoundedNumericValueConstraint extends AbstractBoundedConstraint<Integer, Integer> {

  public BoundedNumericValueConstraint() {
    this(Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  public BoundedNumericValueConstraint(final int minValue, final int maxValue) {
    Assert.greaterThanEqual(maxValue, minValue, "The maximum value must be greater than or equal to the minimum value!");
    setLowerBound(minValue);
    setUpperBound(maxValue);
  }

  public static BoundedNumericValueConstraint getLowerBoundedConstraint(final int minValue) {
    return new BoundedNumericValueConstraint(minValue, Integer.MAX_VALUE);
  }

  public static BoundedNumericValueConstraint getLowerAndUpperBoundedConstraint(final int minValue, final int maxValue) {
    return new BoundedNumericValueConstraint(minValue, maxValue);
  }

  public static BoundedNumericValueConstraint getUpperBoundedConstraint(final int maxValue) {
    return new BoundedNumericValueConstraint(Integer.MIN_VALUE, maxValue);
  }

  public boolean accept(final Integer value) {
    return RelationalOperator.getGreaterThanEqualToAndLessThanEqualTo(getLowerBound(), getUpperBound()).accept(value);
  }

}
