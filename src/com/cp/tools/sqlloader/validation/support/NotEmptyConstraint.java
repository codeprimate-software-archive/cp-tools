
package com.cp.tools.sqlloader.validation.support;

import com.cp.tools.sqlloader.validation.AbstractConstraint;
import com.cp.common.lang.StringUtil;

public final class NotEmptyConstraint extends AbstractConstraint<String> {

  public static final NotEmptyConstraint INSTANCE = new NotEmptyConstraint();

  private NotEmptyConstraint() {
  }

  public boolean accept(final String value) {
    return StringUtil.isNotEmpty(value);
  }

}
