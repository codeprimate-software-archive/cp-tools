
package com.cp.tools.sqlloader.validation.support;

import com.cp.common.util.DateUtil;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BoundedTimeConstraintTest extends TestCase {

  public BoundedTimeConstraintTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BoundedTimeConstraintTest.class);
    return suite;
  }

  public void testInstantiate() throws Exception {
    BoundedTimeConstraint constraint = null;

    try {
      constraint = new BoundedTimeConstraint();
    }
    catch (Exception e) {
      fail("Constructing an instance of the BoundedTimeConstraint using the default constructor should not have thrown an Exception!");
    }

    assertNotNull(constraint);
    constraint = null;
    assertNull(constraint);

    try {
      constraint = new BoundedTimeConstraint(DateUtil.getCalendar(2000, Calendar.JANUARY, 1), DateUtil.getCalendar(2009, Calendar.DECEMBER, 31));
    }
    catch (Exception e) {
      fail("Constructing an instance of the BoundedTimeConstraint with minimum and maximum dates should not have thrown an Exception!");
    }

    assertNotNull(constraint);
  }

  public void testInstantiateThrowsException() throws Exception {
    BoundedTimeConstraint constraint = null;

    try {
      constraint = new BoundedTimeConstraint(null, Calendar.getInstance());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the BoundedTimeConstraint class with a null min date threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(constraint);
    constraint = null;

    try {
      constraint = new BoundedTimeConstraint(Calendar.getInstance(), null);
    }
    catch (Exception e) {
      fail("Instantiating an instance of the BoundedTimeConstraint class with a null max date threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(constraint);
    constraint = null;

    try {
      constraint = new BoundedTimeConstraint(null, null);
    }
    catch (Exception e) {
      fail("Instantiating an instance of BoundedTimeConstraint with null min and max dates threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(constraint);
    constraint = null;

    final Calendar now = Calendar.getInstance();

    final Calendar twoYearsAgo = DateUtil.copy(now);
    twoYearsAgo.roll(Calendar.YEAR, -2);

    try {
      constraint = new BoundedTimeConstraint(now, twoYearsAgo);
      fail("Constructing an instance of the BoundedTimeConstraint with illegal minimum and maximum dates should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The maximum date (" + DateUtil.toString(twoYearsAgo) + ") must be on or after minimum date ("
        + DateUtil.toString(now) + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Constructing an instance of the BoundedTimeConstraint with illegal minimum and maximum dates threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(constraint);
  }

  public void testAcceptMinDate() throws Exception {
    assertFalse(BoundedTimeConstraint.getOnOrAfterConstraint(DateUtil.getCalendar(2008, Calendar.JUNE, 25))
      .accept(DateUtil.getCalendar(2007, Calendar.DECEMBER, 12)));
    assertTrue(BoundedTimeConstraint.getOnOrAfterConstraint(DateUtil.getCalendar(2008, Calendar.JUNE, 25))
      .accept(DateUtil.getCalendar(2008, Calendar.JUNE, 25)));
    assertTrue(BoundedTimeConstraint.getOnOrAfterConstraint(DateUtil.getCalendar(2008, Calendar.JUNE, 25))
      .accept(DateUtil.getCalendar(2009, Calendar.FEBRUARY, 14)));
  }

  public void testAcceptMaxDate() throws Exception {
    assertTrue(BoundedTimeConstraint.getOnOrBeforeConstraint(DateUtil.getCalendar(2008, Calendar.JUNE, 25))
      .accept(DateUtil.getCalendar(2007, Calendar.DECEMBER, 12)));
    assertTrue(BoundedTimeConstraint.getOnOrBeforeConstraint(DateUtil.getCalendar(2008, Calendar.JUNE, 25))
      .accept(DateUtil.getCalendar(2008, Calendar.JUNE, 25)));
    assertFalse(BoundedTimeConstraint.getOnOrBeforeConstraint(DateUtil.getCalendar(2008, Calendar.JUNE, 25))
      .accept(DateUtil.getCalendar(2009, Calendar.FEBRUARY, 14)));
  }

  public void testAcceptMinMaxDate() throws Exception {
    assertFalse(BoundedTimeConstraint.getOnOrAfterAndOnOrBefore(DateUtil.getCalendar(2008, Calendar.JULY, 1), DateUtil.getCalendar(2009, Calendar.JUNE, 30))
      .accept(DateUtil.getCalendar(2007, Calendar.DECEMBER, 12)));
    assertTrue(BoundedTimeConstraint.getOnOrAfterAndOnOrBefore(DateUtil.getCalendar(2008, Calendar.JULY, 1), DateUtil.getCalendar(2009, Calendar.JUNE, 30))
      .accept(DateUtil.getCalendar(2008, Calendar.JULY, 1)));
    assertTrue(BoundedTimeConstraint.getOnOrAfterAndOnOrBefore(DateUtil.getCalendar(2008, Calendar.JULY, 1), DateUtil.getCalendar(2009, Calendar.JUNE, 30))
      .accept(DateUtil.getCalendar(2008, Calendar.DECEMBER, 29)));
    assertTrue(BoundedTimeConstraint.getOnOrAfterAndOnOrBefore(DateUtil.getCalendar(2008, Calendar.JULY, 1), DateUtil.getCalendar(2009, Calendar.JUNE, 30))
      .accept(DateUtil.getCalendar(2009, Calendar.JUNE, 30)));
    assertFalse(BoundedTimeConstraint.getOnOrAfterAndOnOrBefore(DateUtil.getCalendar(2008, Calendar.JULY, 1), DateUtil.getCalendar(2009, Calendar.JUNE, 30))
      .accept(DateUtil.getCalendar(2010, Calendar.FEBRUARY, 14)));
  }

}
