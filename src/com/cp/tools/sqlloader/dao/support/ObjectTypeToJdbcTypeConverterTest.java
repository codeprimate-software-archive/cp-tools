
package com.cp.tools.sqlloader.dao.support;

import com.cp.common.beans.util.converters.AbstractConverter;
import com.cp.common.util.DateUtil;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.beanutils.Converter;

public class ObjectTypeToJdbcTypeConverterTest extends TestCase {

  public ObjectTypeToJdbcTypeConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ObjectTypeToJdbcTypeConverterTest.class);
    //suite.addTest(new ObjectTypeToJdbcTypeConverterTest("testName"));
    return suite;
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    ObjectTypeToJdbcTypeConverter.reset();
  }

  public void testConvert() throws Exception {
    final Calendar date = DateUtil.getCalendar(2008, Calendar.JULY, 30);
    final Timestamp expectedTimestamp = new Timestamp(date.getTimeInMillis());

    assertNull(ObjectTypeToJdbcTypeConverter.convert(Object.class, null));
    assertEquals(Boolean.TRUE, ObjectTypeToJdbcTypeConverter.convert(Boolean.class, Boolean.TRUE));
    assertEquals('C', ObjectTypeToJdbcTypeConverter.convert(Character.class, 'C'));
    assertEquals(expectedTimestamp, ObjectTypeToJdbcTypeConverter.convert(Calendar.class, date));
    assertEquals(Math.PI, ObjectTypeToJdbcTypeConverter.convert(Double.class, Math.PI));
    assertEquals(2, ObjectTypeToJdbcTypeConverter.convert(Integer.class, 2));
    assertEquals("test", ObjectTypeToJdbcTypeConverter.convert(String.class, "test"));
    assertEquals("null", ObjectTypeToJdbcTypeConverter.convert(String.class, "null"));
  }

  public void testGetConverter() throws Exception {
    final Map<Class, Converter> converterMap = new HashMap<Class, Converter>();
    converterMap.put(Double.class, MockConverter.INSTANCE);
    converterMap.put(Integer.class, MockConverter.INSTANCE);

    assertSame(MockConverter.INSTANCE, ObjectTypeToJdbcTypeConverter.getConverter(converterMap, Double.class));
    assertSame(MockConverter.INSTANCE, ObjectTypeToJdbcTypeConverter.getConverter(converterMap, Integer.class));
    assertNull(ObjectTypeToJdbcTypeConverter.getConverter(converterMap, Float.class));
    assertNull(ObjectTypeToJdbcTypeConverter.getConverter(converterMap, Long.class));
    assertNull(ObjectTypeToJdbcTypeConverter.getConverter(converterMap, Character.class));
    assertNull(ObjectTypeToJdbcTypeConverter.getConverter(converterMap, String.class));

    converterMap.put(Number.class, MockConverter.INSTANCE);

    assertSame(MockConverter.INSTANCE, ObjectTypeToJdbcTypeConverter.getConverter(converterMap, Double.class));
    assertSame(MockConverter.INSTANCE, ObjectTypeToJdbcTypeConverter.getConverter(converterMap, Integer.class));
    assertSame(MockConverter.INSTANCE, ObjectTypeToJdbcTypeConverter.getConverter(converterMap, Float.class));
    assertSame(MockConverter.INSTANCE, ObjectTypeToJdbcTypeConverter.getConverter(converterMap, Long.class));
    assertNull(ObjectTypeToJdbcTypeConverter.getConverter(converterMap, Character.class));
    assertNull(ObjectTypeToJdbcTypeConverter.getConverter(converterMap, String.class));
  }

  public void testGetConverterWithNullConverterMap() throws Exception {
    try {
      ObjectTypeToJdbcTypeConverter.getConverter(null, Object.class);
      fail("Calling getConverter with a null Converter Map should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Object type to JDBC type mapping cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getConverter with a null Converter Map threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testGetConverterWithNullObjectType() throws Exception {
    try {
      ObjectTypeToJdbcTypeConverter.getConverter(new HashMap<Class, Converter>(0), null);
      fail("Calling getConverter with a null Object type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Object type cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getConverter with a null Object type threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testRegisterConverter() throws Exception {
    assertTrue(ObjectTypeToJdbcTypeConverter.registerConverter(Boolean.class, MockConverter.INSTANCE));
    assertTrue(ObjectTypeToJdbcTypeConverter.registerConverter(Character.class, MockConverter.INSTANCE));
    assertTrue(ObjectTypeToJdbcTypeConverter.registerConverter(Calendar.class, CalendarToTimestampConverter.INSTANCE));
    assertTrue(ObjectTypeToJdbcTypeConverter.registerConverter(Number.class, MockConverter.INSTANCE));
    assertTrue(ObjectTypeToJdbcTypeConverter.registerConverter(Double.class, MockConverter.INSTANCE));
    assertTrue(ObjectTypeToJdbcTypeConverter.registerConverter(Integer.class, MockConverter.INSTANCE));
    assertTrue(ObjectTypeToJdbcTypeConverter.registerConverter(String.class, MockConverter.INSTANCE));
    assertTrue(ObjectTypeToJdbcTypeConverter.registerConverter(String.class, StringToUpperVarcharConverter.INSTANCE));
    assertTrue(ObjectTypeToJdbcTypeConverter.registerConverter(Object.class, NullToDefaultValueConverter.INSTANCE));
  }

  public void testRegisterConverterWithNullType() throws Exception {
    try {
      ObjectTypeToJdbcTypeConverter.registerConverter(null, MockConverter.INSTANCE);
      fail("Registering a Converter with a null Class type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Class type of the object to register the Converter for cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Registering a Converter with a null Class type threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testRegisterConverterWithNullConverter() throws Exception {
    try {
      ObjectTypeToJdbcTypeConverter.registerConverter(Object.class, null);
      fail("Registering a null Converter with a specified Object type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Converter used to convert objects of the specified Class type cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Registering a null Converter with a specified Object type threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testUnregisterConverter() throws Exception {
    assertTrue(ObjectTypeToJdbcTypeConverter.registerConverter(Boolean.class, MockConverter.INSTANCE));
    assertTrue(ObjectTypeToJdbcTypeConverter.registerConverter(Calendar.class, CalendarToTimestampConverter.INSTANCE));
    assertTrue(ObjectTypeToJdbcTypeConverter.registerConverter(Number.class, MockConverter.INSTANCE));
    assertTrue(ObjectTypeToJdbcTypeConverter.registerConverter(String.class, StringToUpperVarcharConverter.INSTANCE));
    assertFalse(ObjectTypeToJdbcTypeConverter.unregisterConverter(BOOL.class));
    assertFalse(ObjectTypeToJdbcTypeConverter.unregisterConverter(Character.class));
    assertFalse(ObjectTypeToJdbcTypeConverter.unregisterConverter(Date.class));
    assertFalse(ObjectTypeToJdbcTypeConverter.unregisterConverter(Double.class));
    assertFalse(ObjectTypeToJdbcTypeConverter.unregisterConverter(Integer.class));
    assertTrue(ObjectTypeToJdbcTypeConverter.unregisterConverter(Boolean.class));
    assertFalse(ObjectTypeToJdbcTypeConverter.unregisterConverter(Boolean.class));
    assertTrue(ObjectTypeToJdbcTypeConverter.unregisterConverter(Calendar.class));
    assertFalse(ObjectTypeToJdbcTypeConverter.unregisterConverter(Calendar.class));
    assertTrue(ObjectTypeToJdbcTypeConverter.unregisterConverter(Number.class));
    assertFalse(ObjectTypeToJdbcTypeConverter.unregisterConverter(Number.class));
    assertTrue(ObjectTypeToJdbcTypeConverter.unregisterConverter(String.class));
    assertFalse(ObjectTypeToJdbcTypeConverter.unregisterConverter(String.class));
  }

  protected static final class BOOL {
  }

  protected static final class MockConverter extends AbstractConverter {

    public static final MockConverter INSTANCE = new MockConverter();

    protected Object convertImpl(final Class type, final Object value) {
      return null;
    }
  }

}
