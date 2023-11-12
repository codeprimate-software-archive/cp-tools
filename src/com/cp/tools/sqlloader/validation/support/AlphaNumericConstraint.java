
package com.cp.tools.sqlloader.validation.support;

import com.cp.tools.sqlloader.validation.AbstractConstraint;
import com.cp.common.lang.StringUtil;

public final class AlphaNumericConstraint extends AbstractConstraint<String> {

  public static final AlphaNumericConstraint INSTANCE = new AlphaNumericConstraint();

  private AlphaNumericConstraint() {
  }

  public boolean accept(final String value) {
    return StringUtil.isAlphanumericOnly(value);
  }

}
