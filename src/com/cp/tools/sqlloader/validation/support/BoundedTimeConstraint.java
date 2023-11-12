
package com.cp.tools.sqlloader.validation.support;

import com.cp.tools.sqlloader.validation.AbstractBoundedConstraint;
import com.cp.common.lang.Assert;
import com.cp.common.lang.LogicalOperator;
import com.cp.common.lang.ObjectUtil;
import static com.cp.common.lang.UtilityMethods.is;
import com.cp.common.util.DateUtil;
import java.util.Calendar;

public class BoundedTimeConstraint extends AbstractBoundedConstraint<Calendar, Calendar> {

  public BoundedTimeConstraint() {
    this(Calendar.getInstance(), Calendar.getInstance());
  }

  public BoundedTimeConstraint(final Calendar minDate, final Calendar maxDate) {
    if (ObjectUtil.isNotNull(minDate) && ObjectUtil.isNotNull(maxDate)) {
      Assert.greaterThanEqual(maxDate, minDate, "The maximum date (" + DateUtil.toString(maxDate)
        + ") must be on or after minimum date (" + DateUtil.toString(minDate) + ")!");
    }
    setLowerBound(minDate);
    setUpperBound(maxDate);
  }

  public static BoundedTimeConstraint getOnOrAfterConstraint(final Calendar minDate) {
    return new BoundedTimeConstraint(minDate, null);
  }

  public static BoundedTimeConstraint getOnOrAfterAndOnOrBefore(final Calendar minDate, final Calendar maxDate) {
    return new BoundedTimeConstraint(minDate, maxDate);
  }

  public static BoundedTimeConstraint getOnOrBeforeConstraint(final Calendar maxDate) {
    return new BoundedTimeConstraint(null, maxDate);
  }

  public boolean accept(final Calendar value) {
    return LogicalOperator.AND.op(is(value).onOrAfter(getLowerBound(value)), is(value).onOrBefore(getUpperBound(value)));
  }

}
