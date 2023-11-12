
package com.cp.tools.sqlloader.validation;

import static com.cp.common.lang.UtilityMethods.is;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractConstraint<T> implements Constraint<T> {

  protected final Log logger = LogFactory.getLog(getClass());

  public void validate(final T value) throws ConstraintValidationException {
    if (is(accept(value)).False()) {
      throw new ConstraintValidationException("Constraint validation failed!");
    }
  }

}
