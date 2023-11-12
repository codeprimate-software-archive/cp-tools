
package com.cp.tools.sqlloader.validation;

import com.cp.common.lang.Assert;
import com.cp.common.lang.LogicalOperator;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ArrayUtil;
import com.cp.common.util.CollectionUtil;
import java.util.Collection;

// @see com.cp.tools.sqlloader.validation.Constraint
public class ComposableConstraint<T> implements Constraint<T> {

  protected static final LogicalOperator DEFAULT_OPERATOR = LogicalOperator.AND;

  private final Constraint<T> leftConstraint;
  private final Constraint<T> rightConstraint;

  private final LogicalOperator op;

  protected ComposableConstraint(final Constraint<T> leftConstraint, final Constraint<T> rightConstraint, final LogicalOperator op) {
    Assert.notNull(leftConstraint, "The left constraint cannot be null!");
    Assert.notNull(rightConstraint, "The right constraint cannot be null!");
    this.leftConstraint = leftConstraint;
    this.rightConstraint = rightConstraint;
    this.op = ObjectUtil.getDefaultValue(op, DEFAULT_OPERATOR);
  }

  public static <T> Constraint<T> compose(final Constraint<T> leftConstraint,
                                          final Constraint<T> rightConstraint,
                                          final LogicalOperator op)
  {
    return (ObjectUtil.isNull(leftConstraint) ? rightConstraint : (ObjectUtil.isNull(rightConstraint) ? leftConstraint
      : new ComposableConstraint<T>(leftConstraint, rightConstraint, op)));
  }

  public static <T> Constraint<T> compose(final LogicalOperator op, final Constraint<T>... constraints) {
    Constraint<T> previousConstraint = null;

    if (ArrayUtil.isNotEmpty(constraints)) {
      for (final Constraint<T> currentConstraint : constraints) {
        previousConstraint = compose(previousConstraint, currentConstraint, op);
      }
    }

    return previousConstraint;
  }

  public static <T> Constraint<T> compose(final LogicalOperator op, final Collection<Constraint<T>> constraints) {
    Constraint<T> previousConstraint = null;

    if (CollectionUtil.isNotEmpty(constraints)) {
      for (final Constraint<T> currentConstraint : constraints) {
        previousConstraint = compose(op, previousConstraint, currentConstraint);
      }
    }

    return previousConstraint;
  }

  public static <T> Constraint<T> composeAnd(final Constraint<T>... constraints) {
    return compose(LogicalOperator.AND, constraints);
  }

  public static <T> Constraint<T> composeAnd(final Collection<Constraint<T>> constraints) {
    return compose(LogicalOperator.AND, constraints);
  }

  public static <T> Constraint<T> composeOr(final Constraint<T>... constraints) {
    return compose(LogicalOperator.OR, constraints);
  }

  public static <T> Constraint<T> composeOr(final Collection<Constraint<T>> constraints) {
    return compose(LogicalOperator.OR, constraints);
  }

  protected Constraint<T> getLeftConstraint() {
    return leftConstraint;
  }

  protected LogicalOperator getOp() {
    return op;
  }

  protected Constraint<T> getRightConstraint() {
    return rightConstraint;
  }

  public boolean accept(final T value) {
    return getOp().op(getLeftConstraint().accept(value), getRightConstraint().accept(value));
  }

  public void validate(final T value) throws ConstraintValidationException {
    getLeftConstraint().validate(value);
    getRightConstraint().validate(value);
  }

}
