
package com.cp.tools.sqlloader.validation.support;

import com.cp.tools.sqlloader.validation.AbstractConstraint;
import com.cp.common.lang.StringUtil;

public final class DigitsOnlyConstraint extends AbstractConstraint<String> {

  public static final DigitsOnlyConstraint INSTANCE = new DigitsOnlyConstraint();

  private DigitsOnlyConstraint() {
  }

  public boolean accept(final String value) {
    return StringUtil.isDigitsOnly(value);
  }

}
