
package com.cp.tools.sqlloader.validation;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;

public abstract class AbstractBoundedConstraint<B, T> extends AbstractConstraint<T> {

  private B lowerBound;
  private B upperBound;

  public B getLowerBound() {
    return lowerBound;
  }

  protected B getLowerBound(final B value) {
    return ObjectUtil.getDefaultValue(lowerBound, value);
  }

  protected void setLowerBound(final B lowerBound) {
    this.lowerBound = lowerBound;
  }

  public B getUpperBound() {
    return upperBound;
  }

  protected B getUpperBound(final B value) {
    return ObjectUtil.getDefaultValue(upperBound, value);
  }

  protected void setUpperBound(final B upperBound) {
    this.upperBound = upperBound;
  }

  // TODO remove this method once the StringUtil class has been refactored! 
  protected int length(final String value) {
    return (StringUtil.isNotEmpty(value) ? value.length() : 0);
  }

}
