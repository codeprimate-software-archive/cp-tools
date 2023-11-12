/*
 * AllUnitTests.java (c) 21 November 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.11.25
 * @see junit.framework.TestCase
 */

package com.cp.tools.test;

import com.cp.tools.googlechart4j.test.AllGoogleChartTests;
import com.cp.tools.sqlloader.test.AllSqlLoaderTests;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllUnitTests extends TestCase {

  public AllUnitTests(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTest(AllGoogleChartTests.suite());
    suite.addTest(AllSqlLoaderTests.suite());
    return suite;
  }

}
