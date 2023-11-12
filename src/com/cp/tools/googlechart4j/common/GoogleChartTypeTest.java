
package com.cp.tools.googlechart4j.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GoogleChartTypeTest extends TestCase {

  public GoogleChartTypeTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(GoogleChartTypeTest.class);
    return suite;
  }

  public void testGetGoogleChartTypeByCode() throws Exception {
    assertNull(GoogleChartType.getGoogleChartTypeByCode(null));
    assertNull(GoogleChartType.getGoogleChartTypeByCode(""));
    assertNull(GoogleChartType.getGoogleChartTypeByCode("  "));
    assertNull(GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.MAP.getCode().toUpperCase()));
    assertSame(GoogleChartType.COORD_LINE_CHART, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.COORD_LINE_CHART.getCode()));
    assertSame(GoogleChartType.GOOGLE_O_METER, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.GOOGLE_O_METER.getCode()));
    assertSame(GoogleChartType.HORIZONTAL_GROUPED_BAR_CHART, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.HORIZONTAL_GROUPED_BAR_CHART.getCode()));
    assertSame(GoogleChartType.HORIZONTAL_STACKED_BAR_CHART, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.HORIZONTAL_STACKED_BAR_CHART.getCode()));
    assertSame(GoogleChartType.LINE_CHART, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.LINE_CHART.getCode()));
    assertSame(GoogleChartType.MAP, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.MAP.getCode()));
    assertSame(GoogleChartType.PIE_CHART, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.PIE_CHART.getCode()));
    assertSame(GoogleChartType.QR_CHART, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.QR_CHART.getCode()));
    assertSame(GoogleChartType.RADAR_CHART, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.RADAR_CHART.getCode()));
    assertSame(GoogleChartType.SCATTER_PLOT, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.SCATTER_PLOT.getCode()));
    assertSame(GoogleChartType.SPARKLINE, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.SPARKLINE.getCode()));
    assertSame(GoogleChartType.THREE_D_PIE_CHART, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.THREE_D_PIE_CHART.getCode()));
    assertSame(GoogleChartType.VENN_DIAGRAM, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.VENN_DIAGRAM.getCode()));
    assertSame(GoogleChartType.VERTICAL_GROUPED_BAR_CHART, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.VERTICAL_GROUPED_BAR_CHART.getCode()));
    assertSame(GoogleChartType.VERTICAL_STACKED_BAR_CHART, GoogleChartType.getGoogleChartTypeByCode(GoogleChartType.VERTICAL_STACKED_BAR_CHART.getCode()));
  }

  public void testGetGoogleChartTypeByDescription() throws Exception {
    assertNull(GoogleChartType.getGoogleChartTypeByDescription(null));
    assertNull(GoogleChartType.getGoogleChartTypeByDescription(""));
    assertNull(GoogleChartType.getGoogleChartTypeByDescription("  "));
    assertNull(GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.MAP.getDescription().toUpperCase()));
    assertSame(GoogleChartType.COORD_LINE_CHART, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.COORD_LINE_CHART.getDescription()));
    assertSame(GoogleChartType.GOOGLE_O_METER, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.GOOGLE_O_METER.getDescription()));
    assertSame(GoogleChartType.HORIZONTAL_GROUPED_BAR_CHART, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.HORIZONTAL_GROUPED_BAR_CHART.getDescription()));
    assertSame(GoogleChartType.HORIZONTAL_STACKED_BAR_CHART, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.HORIZONTAL_STACKED_BAR_CHART.getDescription()));
    assertSame(GoogleChartType.LINE_CHART, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.LINE_CHART.getDescription()));
    assertSame(GoogleChartType.MAP, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.MAP.getDescription()));
    assertSame(GoogleChartType.PIE_CHART, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.PIE_CHART.getDescription()));
    assertSame(GoogleChartType.QR_CHART, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.QR_CHART.getDescription()));
    assertSame(GoogleChartType.RADAR_CHART, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.RADAR_CHART.getDescription()));
    assertSame(GoogleChartType.SCATTER_PLOT, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.SCATTER_PLOT.getDescription()));
    assertSame(GoogleChartType.SPARKLINE, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.SPARKLINE.getDescription()));
    assertSame(GoogleChartType.THREE_D_PIE_CHART, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.THREE_D_PIE_CHART.getDescription()));
    assertSame(GoogleChartType.VENN_DIAGRAM, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.VENN_DIAGRAM.getDescription()));
    assertSame(GoogleChartType.VERTICAL_GROUPED_BAR_CHART, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.VERTICAL_GROUPED_BAR_CHART.getDescription()));
    assertSame(GoogleChartType.VERTICAL_STACKED_BAR_CHART, GoogleChartType.getGoogleChartTypeByDescription(GoogleChartType.VERTICAL_STACKED_BAR_CHART.getDescription()));
  }

}
