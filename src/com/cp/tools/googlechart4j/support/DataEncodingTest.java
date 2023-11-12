
package com.cp.tools.googlechart4j.support;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DataEncodingTest extends TestCase {

  public DataEncodingTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DataEncodingTest.class);
    return suite;
  }

  public void testGetDataEncodingByCode() throws Exception {
    assertNull(DataEncoding.getDataEncodingByCode(null));
    assertNull(DataEncoding.getDataEncodingByCode(""));
    assertNull(DataEncoding.getDataEncodingByCode(" "));
    assertNull(DataEncoding.getDataEncodingByCode(DataEncoding.SIMPLE.getCode().toUpperCase()));
    assertEquals(DataEncoding.EXTENDED, DataEncoding.getDataEncodingByCode(DataEncoding.EXTENDED.getCode()));
    assertEquals(DataEncoding.SIMPLE, DataEncoding.getDataEncodingByCode(DataEncoding.SIMPLE.getCode()));
    assertEquals(DataEncoding.TEXT, DataEncoding.getDataEncodingByCode(DataEncoding.TEXT.getCode()));
  }

}
