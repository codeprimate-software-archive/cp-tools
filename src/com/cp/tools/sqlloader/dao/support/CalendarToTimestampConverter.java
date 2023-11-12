
package com.cp.tools.sqlloader.dao.support;

import com.cp.common.beans.util.converters.AbstractConverter;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.DateUtil;
import java.sql.Timestamp;
import java.util.Calendar;

public class CalendarToTimestampConverter extends AbstractConverter {

  public static final CalendarToTimestampConverter INSTANCE = new CalendarToTimestampConverter();

  protected Object convertImpl(final Class type, final Object value) {
    if (ObjectUtil.isNotNull(value)) {
      Assert.isInstanceOf(value.getClass(), Calendar.class, "The Object value must of type Calendar!");

      final Calendar calendarValue = (Calendar) value;

      if (logger.isDebugEnabled()) {
        logger.debug("Calendar value (" + DateUtil.toString(calendarValue) + ")");
      }

      return new Timestamp(calendarValue.getTimeInMillis());
    }

    return null;
  }

}
