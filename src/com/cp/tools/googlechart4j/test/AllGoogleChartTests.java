
package com.cp.tools.googlechart4j.test;

import com.cp.tools.googlechart4j.common.AbstractGoogleChartTest;
import com.cp.tools.googlechart4j.common.GoogleChartTypeTest;
import com.cp.tools.googlechart4j.support.AbstractDataSetTest;
import com.cp.tools.googlechart4j.support.DataEncodingTest;
import com.cp.tools.googlechart4j.support.ScaleTest;
import com.cp.tools.googlechart4j.support.TextDataSetTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllGoogleChartTests extends TestCase {

  public AllGoogleChartTests(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTest(getCommonTests());
    suite.addTest(getSupportTests());
    return suite;
  }

  private static Test getCommonTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(AbstractGoogleChartTest.suite());
    suite.addTest(GoogleChartTypeTest.suite());
    return suite;
  }

  private static Test getSupportTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(AbstractDataSetTest.suite());
    suite.addTest(DataEncodingTest.suite());
    suite.addTest(ScaleTest.suite());
    suite.addTest(TextDataSetTest.suite());
    return suite;
  }

}
