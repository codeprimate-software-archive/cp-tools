
package com.cp.tools.sqlloader.validation;

public interface Constraint<T> {

  public boolean accept(T value);

  public void validate(T value) throws ConstraintValidationException;

}
