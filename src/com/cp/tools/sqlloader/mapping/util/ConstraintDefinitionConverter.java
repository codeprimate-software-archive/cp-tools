
package com.cp.tools.sqlloader.mapping.util;

import com.cp.tools.sqlloader.mapping.beans.ConstraintDefinition;
import com.cp.tools.sqlloader.validation.Constraint;
import com.cp.tools.sqlloader.validation.support.ConstraintFactory;
import com.cp.common.beans.util.ConvertUtil;
import com.cp.common.beans.util.converters.CalendarConverter;
import com.cp.common.lang.Assert;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

public final class ConstraintDefinitionConverter {

  private static final Map<ConstraintType, Constraint> CONSTRAINT_MAP = new TreeMap<ConstraintType, Constraint>();

  static {
    CONSTRAINT_MAP.put(ConstraintType.ALPHA_NUMERIC, ConstraintFactory.getAlhpaNumericConstraint());
    CONSTRAINT_MAP.put(ConstraintType.DEFAULT, ConstraintFactory.getDefaultConstraint());
    CONSTRAINT_MAP.put(ConstraintType.DIGITS_ONLY, ConstraintFactory.getDigitsOnlyConstraint());
    CONSTRAINT_MAP.put(ConstraintType.LETTERS_ONLY, ConstraintFactory.getLettersOnlyConstraint());
    CONSTRAINT_MAP.put(ConstraintType.NOT_EMPTY, ConstraintFactory.getNotEmptyConstraint());
    CONSTRAINT_MAP.put(ConstraintType.NOT_NULL, ConstraintFactory.getNotNullConstraint());
  }

  private ConstraintDefinitionConverter() {
  }

  public static Constraint toConstraint(final ConstraintDefinition constraintDefinition) {
    Assert.notNull(constraintDefinition, "The constraint definition cannot be null!");

    final ConstraintType constraintType = ConstraintType.getByName(constraintDefinition.getName());
    Assert.notNull(constraintType, "Constraint having name (" + constraintDefinition.getName() + ") is not a valid constraint!");

    if (CONSTRAINT_MAP.containsKey(constraintType)) {
      return CONSTRAINT_MAP.get(constraintType);
    }
    else {
      switch (constraintType) {
        case BOUNDED_LENGTH:
          return ConstraintFactory.getBoundedStringLengthConstraint(Integer.parseInt(constraintDefinition.getMin()),
            Integer.parseInt(constraintDefinition.getMax()));
        case BOUNDED_NUMERIC:
          return ConstraintFactory.getBoundedNumericValueConstraint(Integer.parseInt(constraintDefinition.getMin()),
            Integer.parseInt(constraintDefinition.getMax()));
        case BOUNDED_TIME:
          CalendarConverter.addDateFormatPattern(constraintDefinition.getPattern());

          Calendar minDate = (Calendar) ConvertUtil.convert(Calendar.class, constraintDefinition.getMin());
          Calendar maxDate = (Calendar) ConvertUtil.convert(Calendar.class, constraintDefinition.getMax());

          return ConstraintFactory.getBoundedTimeConstraint(minDate, maxDate);
        case REGEX:
          return ConstraintFactory.getRegexConstraint(constraintDefinition.getPattern());
        case UNIQUE:
          return ConstraintFactory.getUniqueConstraint();
        default:
          return ConstraintFactory.getDefaultConstraint();
      }
    }
  }

}
