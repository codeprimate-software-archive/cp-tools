
package com.cp.tools.sqlloader.validation.support;

import com.cp.tools.sqlloader.validation.AbstractConstraint;
import com.cp.common.lang.ObjectUtil;

public final class NotNullConstraint extends AbstractConstraint<Object> {

  public static final NotNullConstraint INSTANCE = new NotNullConstraint();

  private NotNullConstraint() {
  }

  public boolean accept(final Object value) {
    return ObjectUtil.isNotNull(value);
  }

}
