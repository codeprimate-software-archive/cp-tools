
package com.cp.tools.googlechart4j.support;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractDataSetTest extends TestCase {

  public AbstractDataSetTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractDataSetTest.class);
    return suite;
  }

  protected AbstractDataSet getDataSet(final DataEncoding encoding) {
    return new TestDataSet(encoding);
  }

  public void testInstantiation() throws Exception {
    AbstractDataSet dataSet = null;

    assertNull(dataSet);

    try {
      dataSet = getDataSet(DataEncoding.SIMPLE);
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractDataSet with a non-null encoding threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(dataSet);
    assertEquals(DataEncoding.SIMPLE, dataSet.getEncoding());
  }

  public void testIntantiationWithNullDataEncoding() throws Exception {
    AbstractDataSet dataSet = null;

    assertNull(dataSet);

    try {
      dataSet = getDataSet(null);
      fail("Instantiating an instance of AbstractDataSet with a null encoding should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The data encoding for this DataSet cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractDataSet with a null encoding threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(dataSet);
  }

  protected static final class TestDataSet extends AbstractDataSet {

    public TestDataSet(final DataEncoding encoding) {
      super(encoding);
    }

    public String encode() {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

}
