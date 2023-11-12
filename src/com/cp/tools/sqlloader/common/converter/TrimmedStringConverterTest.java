
package com.cp.tools.sqlloader.common.converter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.beanutils.Converter;

public class TrimmedStringConverterTest extends TestCase {

  public TrimmedStringConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(TrimmedStringConverterTest.class);
    return suite;
  }

  public void testConvert() throws Exception {
    final Converter trimmedStringConverter = new TrimmedStringConverter();

    assertNull(trimmedStringConverter.convert(String.class, null));
    assertNull(trimmedStringConverter.convert(String.class, "null"));
    assertNull(trimmedStringConverter.convert(String.class, " null "));
    assertEquals("", trimmedStringConverter.convert(String.class, ""));
    assertEquals("", trimmedStringConverter.convert(String.class, " "));
    assertEquals("", trimmedStringConverter.convert(String.class, "   "));
    assertEquals("empty", trimmedStringConverter.convert(String.class, "empty"));
    assertEquals("empty", trimmedStringConverter.convert(String.class, " empty "));
    assertEquals("emp ty", trimmedStringConverter.convert(String.class, " emp ty   "));
  }

}
