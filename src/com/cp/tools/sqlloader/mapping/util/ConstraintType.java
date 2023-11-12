
package com.cp.tools.sqlloader.mapping.util;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;

public enum ConstraintType {
  ALPHA_NUMERIC("alphaNumeric", "Letters and Digits Only Constraint"),
  BOUNDED_LENGTH("boundedLength", "Minimum and Maxium String Length Constraint"),
  BOUNDED_NUMERIC("boundedNumeric", "Minimum and Maxium Numeric Value Constraint"),
  BOUNDED_TIME("boundedTime", "Period of Time Constraint"),
  DEFAULT("default", "Default Constraint"),
  DIGITS_ONLY("digitsOnly", "String containing Digits Only Constraint"),
  LETTERS_ONLY("lettersOnly", "String containing Letters Only Constraint"),
  NOT_EMPTY("notEmpty", "Non-Empty String Constraint"),
  NOT_NULL("notNull", "Not Null Value Constraint"),
  REGEX("regex", "Regular Expression Constraint"),
  UNIQUE("unique", "Unique Constraint");

  private final String description;
  private final String name;

  ConstraintType(final String name, final String description) {
    Assert.notEmpty(name, "The constraint type name cannot be null or empty!");
    Assert.notEmpty(description, "The constraint type description cannot be null or empty!");
    this.name = name;
    this.description = description;
  }

  public static ConstraintType getByName(final String name) {
    for (final ConstraintType constraintType : values()) {
      if (ObjectUtil.equals(constraintType.getName(), name)) {
        return constraintType;
      }
    }

    return null;
  }

  public String getDescription() {
    return description;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer(getName());
    buffer.append(" (").append(getDescription()).append(")");
    return buffer.toString();
  }

}
