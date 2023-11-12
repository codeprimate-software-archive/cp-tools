
package com.cp.tools.sqlloader.validation;

public class ConstraintValidationException extends Exception {

  public ConstraintValidationException() {
  }

  public ConstraintValidationException(final String message) {
    super(message);
  }

  public ConstraintValidationException(final Throwable cause) {
    super(cause);
  }

  public ConstraintValidationException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
