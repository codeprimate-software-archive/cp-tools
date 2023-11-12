
package com.cp.tools.sqlloader.validation.support;

import com.cp.tools.sqlloader.validation.AbstractConstraint;
import com.cp.common.lang.StringUtil;

public final class LettersOnlyConstraint extends AbstractConstraint<String> {

  public static final LettersOnlyConstraint INSTANCE = new LettersOnlyConstraint();

  private LettersOnlyConstraint() {
  }

  public boolean accept(final String value) {
    return StringUtil.isLettersOnly(value);
  }

}
