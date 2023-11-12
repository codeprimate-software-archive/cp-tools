
package com.cp.tools.sqlloader.common.parser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ParserUtilTest extends TestCase {

  public ParserUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ParserUtilTest.class);
    return suite;
  }

  public void testParseInt() throws Exception {
    assertEquals(-1, ParserUtil.parseInt(null));
    assertEquals(-1, ParserUtil.parseInt(""));
    assertEquals(-1, ParserUtil.parseInt(" "));
    assertEquals(-1, ParserUtil.parseInt("-1"));
    assertEquals(0, ParserUtil.parseInt("0"));
    assertEquals(1, ParserUtil.parseInt("1"));
    assertEquals(2, ParserUtil.parseInt("2"));
    assertEquals(101, ParserUtil.parseInt("101"));
  }

}
