
package com.cp.tools.googlechart4j.support;

import com.cp.tools.googlechart4j.util.GoogleChartUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TextDataSetTest extends TestCase {

  public TextDataSetTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(TextDataSetTest.class);
    return suite;
  }

  protected void assertEquals(final float[] expectedValues, final float[] actualValues) {
    assertEquals(expectedValues.length, actualValues.length);

    int index = 0;

    for (final float expectedValue : expectedValues) {
      assertEquals(expectedValue, actualValues[index++]);
    }
  }

  protected TextDataSet getDataSet(final float... values) {
    return new TextDataSet(values);
  }

  protected TextDataSet getDataSet(final Scale scale, final float... values) {
    return new TextDataSet(scale, values);
  }

  public void testEncode() throws Exception {
    final TextDataSet dataSet = getDataSet(10.0f, 20.0f, 40.0f, 80.0f);

    assertNotNull(dataSet);
    assertEquals(new Scale(0, 100), dataSet.getScale());
    assertEquals(new float[] { 10.0f, 20.0f, 40.0f, 80.0f }, dataSet.getValues());

    final StringBuffer expectedEncoding = new StringBuffer("chd=");
    expectedEncoding.append(DataEncoding.TEXT.getCode());
    expectedEncoding.append(":10.0,20.0,40.0,80.0");
    expectedEncoding.append(GoogleChartUtil.PARAMETER_DELIMITER);
    expectedEncoding.append("chds=0,100");

    assertEquals(expectedEncoding.toString(), dataSet.encode());
  }

  public void testInstantiate() throws Exception {
    TextDataSet dataSet = null;

    assertNull(dataSet);

    try {
      dataSet = getDataSet(new Scale(-10, 10), -5.0f, 0.0f, 5.0f);
    }
    catch (Exception e) {
      fail("Instantiating an instance of TextDataSet with a valid scale and floating-point values threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(dataSet);
    assertEquals(new Scale(-10, 10), dataSet.getScale());
    assertEquals(new float[] { -5.0f, 0.0f, 5.0f }, dataSet.getValues());
  }

  public void testIntantiateWithEmptyValues() throws Exception {
    TextDataSet dataSet = null;

    assertNull(dataSet);

    try {
      dataSet = getDataSet(new Scale(0, 1), new float[0]);
      fail("Instantiating an instance of TextDataSet with an empty value array should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The array of floating point numbers defining the chart data cannot be empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of TextDataSet with an empty value array threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(dataSet);
  }

  public void testInstantiateWithInvalidLowerBoundValues() throws Exception {
    TextDataSet dataSet = null;

    assertNull(dataSet);

    try {
      dataSet = getDataSet(new Scale(0, 1), 0.0f, 0.1f, 0.21f, 0.421f, 0.8421f, -0.0f, -0.1f);
      fail("Instantiating an instance of TextDataSet with invalid lower bound values should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The floating point value (-0.1) must be greater or equal to (0)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of TextDataSet with invalid lower bound values threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(dataSet);
  }

  public void testInstantiateWithInvalidUpperBoundValues() throws Exception {
    TextDataSet dataSet = null;

    assertNull(dataSet);

    try {
      dataSet = getDataSet(0.0f, 10.0f, 20.0f, 40.0f, 80.0f, 100.0f, 100.1f);
      fail("Instantiating an instance of TextDataSet with invalid upper bound values should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The floating point value (100.1) must be less than or equal to (100)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of TextDataSet with invalid upper bound values threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(dataSet);
  }

  public void testInstantiateWithNullScale() throws Exception {
    TextDataSet dataSet = null;

    assertNull(dataSet);

    try {
      dataSet = getDataSet(null, 0.0f, 50.0f, 100.0f);
    }
    catch (Exception e) {
      fail("Instantiating an instance of TextDataSet with a null scale and floating-point values threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(dataSet);
    assertEquals(new Scale(0, 100), dataSet.getScale());
    assertEquals(new float[] { 0.0f, 50.0f, 100.0f }, dataSet.getValues());
  }

  public void testInstantiateWithNullValues() throws Exception {
    TextDataSet dataSet = null;

    assertNull(dataSet);

    try {
      dataSet = getDataSet(new Scale(0, 1), (float[]) null);
      fail("Instantiating an instance of TextDataSet with null values should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The array of floating point numbers defining the chart data cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of TextDataSet with null values threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(dataSet);
  }

}
