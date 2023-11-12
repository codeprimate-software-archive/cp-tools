
package com.cp.tools.sqlloader.dao.support;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StringToUpperVarcharConverterTest extends TestCase {

  public StringToUpperVarcharConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(StringToUpperVarcharConverterTest.class);
    return suite;
  }

  public void testConvert() throws Exception {
    assertNull(StringToUpperVarcharConverter.INSTANCE.convert(String.class, null));
    assertNull(StringToUpperVarcharConverter.INSTANCE.convert(String.class, "null"));
    assertNull(StringToUpperVarcharConverter.INSTANCE.convert(String.class, "Null"));
    assertNull(StringToUpperVarcharConverter.INSTANCE.convert(String.class, "NULL"));
    assertEquals("NILL", StringToUpperVarcharConverter.INSTANCE.convert(String.class, "nill"));
    assertEquals("TEST", StringToUpperVarcharConverter.INSTANCE.convert(String.class, "test"));
    assertEquals("THIS IS A TITLE CASE STRING!", StringToUpperVarcharConverter.INSTANCE.convert(String.class, "This is a Title Case String!"));
  }

  public void testConvertThrowsExcption() throws Exception {
    try {
      StringToUpperVarcharConverter.INSTANCE.convert(String.class, Boolean.TRUE);
      fail("Calling convert with a Boolean value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The Object value must be of type String!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with a Boolean value threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }


}
