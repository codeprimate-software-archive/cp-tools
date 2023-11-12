
package com.cp.tools.googlechart4j.common;

import com.cp.tools.googlechart4j.util.GoogleChartUtil;
import java.awt.Dimension;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractGoogleChartTest extends TestCase {

  public AbstractGoogleChartTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractGoogleChartTest.class);
    return suite;
  }

  protected AbstractGoogleChart getGoogleChart() {
    return new TestGoogleChart();
  }

  protected AbstractGoogleChart getGoogleChart(final GoogleChartType type, final Dimension size) {
    return new TestGoogleChart(type, size);
  }

  public void testEncodeSize() throws Exception {
    final AbstractGoogleChart googleChart = getGoogleChart(GoogleChartType.MAP, new Dimension(0, 0));

    assertNotNull(googleChart);
    assertEquals(GoogleChartType.MAP, googleChart.getType());
    assertEquals(new Dimension(0, 0), googleChart.getSize());
    assertEquals(0, googleChart.getWidth());
    assertEquals(0, googleChart.getHeight());
    assertEquals(GoogleChartUtil.PARAMETER_DELIMITER + "chs=0x0", googleChart.encodeSize());
  }

  public void testEncodeTitle() throws Exception {
    final AbstractGoogleChart googleChart = getGoogleChart(GoogleChartType.MAP, new Dimension(0, 0));

    assertNotNull(googleChart);
    assertEquals(GoogleChartType.MAP, googleChart.getType());
    assertEquals(new Dimension(0, 0), googleChart.getSize());
    assertEquals(0, googleChart.getWidth());
    assertEquals(0, googleChart.getHeight());
    assertNull(googleChart.getTitle());
    assertEquals("", googleChart.encodeTitle());

    googleChart.setTitle(" ");

    assertEquals("", googleChart.encodeTitle());

    googleChart.setTitle("My Google Chart");

    assertEquals(GoogleChartUtil.PARAMETER_DELIMITER + "chtt=My Google Chart", googleChart.encodeTitle());
  }

  public void testEncodeType() throws Exception {
    final AbstractGoogleChart googleChart = getGoogleChart(GoogleChartType.MAP, new Dimension(0, 0));

    assertNotNull(googleChart);
    assertEquals(GoogleChartType.MAP, googleChart.getType());
    assertEquals(new Dimension(0, 0), googleChart.getSize());
    assertEquals(0, googleChart.getWidth());
    assertEquals(0, googleChart.getHeight());
    assertEquals("cht=" + GoogleChartType.MAP.getCode(), googleChart.encodeType());
  }

  public void testEncodeUrl() throws Exception {
    final AbstractGoogleChart googleChart = getGoogleChart(GoogleChartType.MAP, new Dimension(0, 0));

    assertNotNull(googleChart);
    assertEquals(GoogleChartType.MAP, googleChart.getType());
    assertEquals(new Dimension(0, 0), googleChart.getSize());
    assertEquals(0, googleChart.getWidth());
    assertEquals(0, googleChart.getHeight());
    assertNull(googleChart.getTitle());

    googleChart.setTitle("My Google Chart");

    assertEquals("My Google Chart", googleChart.getTitle());

    final StringBuffer expectedUrl = new StringBuffer(AbstractGoogleChart.BASE_URL_STRING);
    expectedUrl.append("cht=").append(GoogleChartType.MAP.getCode());
    expectedUrl.append(GoogleChartUtil.PARAMETER_DELIMITER);
    expectedUrl.append("chs=0x0");
    expectedUrl.append(GoogleChartUtil.PARAMETER_DELIMITER);
    expectedUrl.append("chtt=My Google Chart");

    assertEquals(expectedUrl.toString(), googleChart.encodeUrl());
  }

  public void testGetSize() throws Exception {
    AbstractGoogleChart googleChart = null;

    assertNull(googleChart);

    try {
      googleChart = getGoogleChart(GoogleChartType.VENN_DIAGRAM, new Dimension(100, 100));
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractGoogleChart with a non-null type and valid size threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(googleChart);
    assertEquals(new Dimension(100, 100), googleChart.getSize());
    assertEquals(100, googleChart.getWidth());
    assertEquals(100, googleChart.getHeight());
  }

  public void testGetSizeThrowsIllegalStateException() throws Exception {
    AbstractGoogleChart googleChart = getGoogleChart();

    assertNotNull(googleChart);

    try {
      googleChart.getSize();
      fail("Calling getSize with an unspecified size should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The size of the Google Chart/Map has not been specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getSize with an unspecified size threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testSetSize() throws Exception {
    AbstractGoogleChart googleChart = getGoogleChart(GoogleChartType.MAP, new Dimension((int) AbstractGoogleChart.EXPECTED_CHART_WIDTH, 300));

    assertNotNull(googleChart);
    assertEquals((int) AbstractGoogleChart.EXPECTED_CHART_WIDTH, googleChart.getWidth());
    assertEquals(300, googleChart.getHeight());

    googleChart = getGoogleChart(GoogleChartType.MAP, new Dimension(300, (int) AbstractGoogleChart.EXPECTED_CHART_HEIGHT));

    assertNotNull(googleChart);
    assertEquals(300, googleChart.getWidth());
    assertEquals((int) AbstractGoogleChart.EXPECTED_CHART_HEIGHT, googleChart.getHeight());
  }

  public void testSetSizeToNull() throws Exception {
    AbstractGoogleChart googleChart = null;

    assertNull(googleChart);

    try {
      googleChart = getGoogleChart(GoogleChartType.GOOGLE_O_METER, null);
      fail("Instantiating an instance of AbstractGoogleChart with a null size should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The size of the Google Chart/Map cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractGoogleChart with a null size threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(googleChart);
  }

  public void testSetSizeWithInvalidArea() throws Exception {
    AbstractGoogleChart googleChart = null;

    assertNull(googleChart);

    try {
      googleChart = getGoogleChart(GoogleChartType.PIE_CHART,
        new Dimension((int) AbstractGoogleChart.EXPECTED_CHART_WIDTH, (int) AbstractGoogleChart.EXPECTED_CHART_HEIGHT));
      fail("Instantiating an instance of AbstractGoogleChart with an invalid area should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual area (" + (AbstractGoogleChart.EXPECTED_CHART_WIDTH * AbstractGoogleChart.EXPECTED_CHART_HEIGHT)
      + ") of the Google Chart/Map must be less than or equal to (" + AbstractGoogleChart.EXPECTED_CHART_AREA + ")!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractGoogleChart with an invalid area threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(googleChart);
  }

  public void testSetSizeWithInvalidHeight() throws Exception {
    AbstractGoogleChart googleChart = null;

    assertNull(googleChart);

    try {
      googleChart = getGoogleChart(GoogleChartType.HORIZONTAL_GROUPED_BAR_CHART, new Dimension(0, -1));
      fail("Instantiating an instance of AbstractGoogleChart with a height of -1 should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual height (-1.0) of the Google Chart/Map must be greater than or equal to 0!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractGoogleChart with a height of -1 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(googleChart);

    try {
      googleChart = getGoogleChart(GoogleChartType.HORIZONTAL_STACKED_BAR_CHART,
        new Dimension(0, (int) (AbstractGoogleChart.EXPECTED_CHART_HEIGHT + 1)));
      fail("Instantiating an instance of AbstractGoogleChart with a height of 1 greater than the expected height should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual height (" + (AbstractGoogleChart.EXPECTED_CHART_HEIGHT + 1)
        + ") of the Google Chart/Map has to be less than or equal to (" + AbstractGoogleChart.EXPECTED_CHART_HEIGHT
        + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractGoogleChart with a height of 1 greater than the expected height threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(googleChart);
  }

  public void testSetSizeWithInvalidWidth() throws Exception {
    AbstractGoogleChart googleChart = null;

    assertNull(googleChart);

    try {
      googleChart = getGoogleChart(GoogleChartType.VERTICAL_GROUPED_BAR_CHART, new Dimension(-1, 0));
      fail("Instantiating an instance of AbstractGoogleChart with a width of -1 should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual width (-1.0) of the Google Chart/Map must be greater than or equal to 0!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractGoogleChart with a width of -1 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(googleChart);

    try {
      googleChart = getGoogleChart(GoogleChartType.VERTICAL_STACKED_BAR_CHART,
        new Dimension((int) (AbstractGoogleChart.EXPECTED_CHART_WIDTH + 1), 0));
      fail("Instantiating an instance of AbstractGoogleChart with a width of 1 greater than the expected width should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The actual width (" + (AbstractGoogleChart.EXPECTED_CHART_WIDTH + 1)
        + ") of the Google Chart/Map has to be less than or equal to (" + AbstractGoogleChart.EXPECTED_CHART_WIDTH
        + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractGoogleChart with a width of 1 greater than the expected width threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(googleChart);
  }

  public void testGetType() throws Exception {
    AbstractGoogleChart googleChart = null;

    assertNull(googleChart);

    try {
      googleChart = getGoogleChart(GoogleChartType.MAP, new Dimension(0, 0));
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractGoogleChart with a map type and valid size threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(googleChart);
    assertEquals(GoogleChartType.MAP, googleChart.getType());
  }

  public void testGetTypeThrowsIllegalStateException() throws Exception {
    final AbstractGoogleChart googleChart = getGoogleChart();

    assertNotNull(googleChart);

    try {
      googleChart.getType();
      fail("Calling getType with an unspecified type should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The type of Google Chart/Map has not been specified!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getType with an unspecified type threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testSetTypeToNull() throws Exception {
    AbstractGoogleChart googleChart = null;

    assertNull(googleChart);

    try {
      googleChart = getGoogleChart(null, new Dimension(0, 0));
      fail("Instantiating an instance of AbstractGoogleChart with a null type should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The type of Google Chart/Map cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of AbstractGoogleChart with a null type threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(googleChart);
  }

  public void testToUrl() throws Exception {
    final AbstractGoogleChart googleChart = getGoogleChart(GoogleChartType.MAP, new Dimension(0, 0));

    assertNotNull(googleChart);
    assertEquals(GoogleChartType.MAP, googleChart.getType());
    assertEquals(new Dimension(0, 0), googleChart.getSize());
    assertEquals(0, googleChart.getWidth());
    assertEquals(0, googleChart.getHeight());

    final StringBuffer expectedUrl = new StringBuffer(AbstractGoogleChart.BASE_URL_STRING);
    expectedUrl.append("cht=").append(GoogleChartType.MAP.getCode());
    expectedUrl.append(GoogleChartUtil.PARAMETER_DELIMITER);
    expectedUrl.append("chs=0x0");

    assertEquals(new URL(expectedUrl.toString()), googleChart.toUrl());
  }

  protected static final class TestGoogleChart extends AbstractGoogleChart {

    public TestGoogleChart() {
    }

    public TestGoogleChart(final GoogleChartType type, final Dimension size) {
      super(type, size);
    }

    public Object getData() {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    public void setData(final Object data) {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

}
