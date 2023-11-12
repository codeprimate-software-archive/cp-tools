
package com.cp.tools.sqlloader.dao.support;

import com.cp.common.util.record.Record;
import java.net.URL;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ObjectTypeToSqlTypeConverterTest extends TestCase {

  public ObjectTypeToSqlTypeConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ObjectTypeToSqlTypeConverterTest.class);
    return suite;
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    ObjectTypeToSqlTypeConverter.reset();
  }

  public void testConvert() throws Exception {
    assertEquals(Types.NULL, ObjectTypeToSqlTypeConverter.convert(null));
    assertEquals(Types.BOOLEAN, ObjectTypeToSqlTypeConverter.convert(Boolean.class));
    assertEquals(Types.CHAR, ObjectTypeToSqlTypeConverter.convert(Character.class));
    assertEquals(Types.TIMESTAMP, ObjectTypeToSqlTypeConverter.convert(Calendar.class));
    assertEquals(Types.DATE, ObjectTypeToSqlTypeConverter.convert(Date.class));
    assertEquals(Types.NUMERIC, ObjectTypeToSqlTypeConverter.convert(Number.class));
    assertEquals(Types.NUMERIC, ObjectTypeToSqlTypeConverter.convert(Double.class));
    assertEquals(Types.NUMERIC, ObjectTypeToSqlTypeConverter.convert(Integer.class));
    assertEquals(Types.VARCHAR, ObjectTypeToSqlTypeConverter.convert(String.class));
  }

  public void testConvertWithNoRegisterObjectType() throws Exception {
    try {
      ObjectTypeToSqlTypeConverter.convert(BOOL.class);
      fail("Calling convert with the BOOL Class type should have thrown a NoObjectTypeToSqlTypeRegisteredException!");
    }
    catch (NoObjectTypeToSqlTypeRegisteredException e) {
      assertEquals("No SQL type is registered for Object type (" + BOOL.class.getName() + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with the BOOL Class type threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testGetSqlType() throws Exception {
    final Map<Class, Integer> sqlTypeMap = new HashMap<Class, Integer>(3);
    sqlTypeMap.put(Double.class, Types.DOUBLE);
    sqlTypeMap.put(Integer.class, Types.INTEGER);

    assertEquals(new Integer(Types.DOUBLE), ObjectTypeToSqlTypeConverter.getSqlType(sqlTypeMap, Double.class));
    assertEquals(new Integer(Types.INTEGER), ObjectTypeToSqlTypeConverter.getSqlType(sqlTypeMap, Integer.class));
    assertNull(ObjectTypeToSqlTypeConverter.getSqlType(sqlTypeMap, Float.class));
    assertNull(ObjectTypeToSqlTypeConverter.getSqlType(sqlTypeMap, Long.class));
    assertNull(ObjectTypeToSqlTypeConverter.getSqlType(sqlTypeMap, Boolean.class));
    assertNull(ObjectTypeToSqlTypeConverter.getSqlType(sqlTypeMap, String.class));

    sqlTypeMap.put(Number.class, Types.NUMERIC);

    assertEquals(new Integer(Types.DOUBLE), ObjectTypeToSqlTypeConverter.getSqlType(sqlTypeMap, Double.class));
    assertEquals(new Integer(Types.INTEGER), ObjectTypeToSqlTypeConverter.getSqlType(sqlTypeMap, Integer.class));
    assertEquals(new Integer(Types.NUMERIC), ObjectTypeToSqlTypeConverter.getSqlType(sqlTypeMap, Float.class));
    assertEquals(new Integer(Types.NUMERIC), ObjectTypeToSqlTypeConverter.getSqlType(sqlTypeMap, Long.class));
    assertNull(ObjectTypeToSqlTypeConverter.getSqlType(sqlTypeMap, Boolean.class));
    assertNull(ObjectTypeToSqlTypeConverter.getSqlType(sqlTypeMap, String.class));
  }

  public void testGetSqlTypeWithNullMap() throws Exception {
    try {
      ObjectTypeToSqlTypeConverter.getSqlType(null, Object.class);
      fail("Calling getSqlType with a null Object type to SQL type Map should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The specified Object type to SQL type mapping cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getSqlType with a null Object type to SQL type Map threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testGetSqlTypeWithNullType() throws Exception {
    try {
      ObjectTypeToSqlTypeConverter.getSqlType(new HashMap<Class, Integer>(0), null);
      fail("Calling getSqlType with a null Class type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Object type cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getSqlType with a null Class type threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testRegisterType() throws Exception {
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(Boolean.class, Types.BIT));
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(Byte.class, Types.TINYINT));
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(Calendar.class, Types.TIME));
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(Character.class, Types.CHAR));
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(Double.class, Types.REAL));
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(Integer.class, Types.DECIMAL));
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(Record.class, Types.STRUCT));
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(String.class, Types.CLOB));
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(URL.class, Types.DATALINK));
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(Object.class, Types.REF));
  }

  public void testRegisterTypeWithNullType() throws Exception {
    try {
      ObjectTypeToSqlTypeConverter.registerType(null, 0);
      fail("Registering a null Class type for a SQL type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Class type cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Registering a null Class type for a SQL type threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testUnregisterType() throws Exception {
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(Boolean.class, Types.BIT));
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(Number.class, Types.NUMERIC));
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(String.class, Types.VARCHAR));
    assertTrue(ObjectTypeToSqlTypeConverter.registerType(Object.class, Types.JAVA_OBJECT));
    assertFalse(ObjectTypeToSqlTypeConverter.unregisterType(BOOL.class));
    assertFalse(ObjectTypeToSqlTypeConverter.unregisterType(Character.class));
    assertFalse(ObjectTypeToSqlTypeConverter.unregisterType(Double.class));
    assertFalse(ObjectTypeToSqlTypeConverter.unregisterType(Integer.class));
    assertTrue(ObjectTypeToSqlTypeConverter.unregisterType(Boolean.class));
    assertFalse(ObjectTypeToSqlTypeConverter.unregisterType(Boolean.class));
    assertTrue(ObjectTypeToSqlTypeConverter.unregisterType(Number.class));
    assertFalse(ObjectTypeToSqlTypeConverter.unregisterType(Number.class));
    assertTrue(ObjectTypeToSqlTypeConverter.unregisterType(String.class));
    assertFalse(ObjectTypeToSqlTypeConverter.unregisterType(String.class));
    assertTrue(ObjectTypeToSqlTypeConverter.unregisterType(Object.class));
    assertFalse(ObjectTypeToSqlTypeConverter.unregisterType(Object.class));
  }

  protected static final class BOOL {
  }

}
