
package com.cp.tools.sqlloader.dao.support;

import com.cp.common.util.DateUtil;
import java.sql.Timestamp;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CalendarToTimestampConverterTest extends TestCase {

  public CalendarToTimestampConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CalendarToTimestampConverterTest.class);
    return suite;
  }

  public void testConvert() throws Exception {
    assertNull(CalendarToTimestampConverter.INSTANCE.convert(Calendar.class, null));
    assertNull(CalendarToTimestampConverter.INSTANCE.convert(Calendar.class, "null"));
    assertNull(CalendarToTimestampConverter.INSTANCE.convert(Calendar.class, " null  "));

    final Calendar date = DateUtil.getCalendar(2008, Calendar.JULY, 25);

    Timestamp expectedTimestamp = new Timestamp(date.getTimeInMillis());
    Object actualTimestamp = CalendarToTimestampConverter.INSTANCE.convert(Calendar.class, date);

    assertTrue(actualTimestamp instanceof Timestamp);
    assertEquals(expectedTimestamp, actualTimestamp);

    final Calendar dateTime = DateUtil.getCalendar(2020, Calendar.SEPTEMBER, 2, 15, 30, 45, 888);
    expectedTimestamp = new Timestamp(dateTime.getTimeInMillis());
    actualTimestamp = CalendarToTimestampConverter.INSTANCE.convert(Calendar.class, dateTime);

    assertTrue(actualTimestamp instanceof Timestamp);
    assertEquals(expectedTimestamp, actualTimestamp);
  }

}
