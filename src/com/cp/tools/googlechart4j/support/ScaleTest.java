
package com.cp.tools.googlechart4j.support;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ScaleTest extends TestCase {

  public ScaleTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ScaleTest.class);
    return suite;
  }

  public void testEncode() throws Exception {
    final Scale scale = new Scale(-9, 9);

    assertNotNull(scale);
    assertEquals(-9, scale.getMinValue());
    assertEquals(9, scale.getMaxValue());
    assertEquals("-9,9", scale.encode());
  }

}
