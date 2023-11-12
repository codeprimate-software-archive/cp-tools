
package com.cp.tools.googlechart4j.common;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;

public enum GoogleChartType {
  LINE_CHART("lc", "Line Chart"),
  COORD_LINE_CHART("lxy", "Line Chart with X,Y Coordinates"),
  SPARKLINE("ls", "Sparkline"),
  HORIZONTAL_GROUPED_BAR_CHART("bhg", "Horizontal Grouped Bar Chart"),
  HORIZONTAL_STACKED_BAR_CHART("bhs", "Horizontal Stacked Bar Chart"),
  VERTICAL_GROUPED_BAR_CHART("bvg", "Vertical Grouped Bar Chart"),
  VERTICAL_STACKED_BAR_CHART("bvs", "Vertical Stacked Bar Chart"),
  PIE_CHART("p", "Pie Chart"),
  THREE_D_PIE_CHART("p3", "3D Pie Chart"),
  VENN_DIAGRAM("v", "Venn Diagram"),
  SCATTER_PLOT("s", "Scatter Plot Chart"),
  RADAR_CHART("r", "Radar Chart"),
  MAP("t", "Map"),
  GOOGLE_O_METER("gom", "Google-o-meter"),
  QR_CHART("qr", "QR Codes");

  private final String code;
  private final String description;

  GoogleChartType(final String code, final String description) {
    Assert.notEmpty(code, " The code specifying the Google chart type cannot be null or empty!");
    Assert.notEmpty(description, "The description of the Google chart type cannot be null or empty!");
    this.code = code;
    this.description = description;
  }

  public static GoogleChartType getGoogleChartTypeByCode(final String code) {
    for (final GoogleChartType chartType : values()) {
      if (ObjectUtil.equals(chartType.getCode(), code)) {
        return chartType;
      }
    }

    return null;
  }

  public static GoogleChartType getGoogleChartTypeByDescription(final String description) {
    for (final GoogleChartType chartType : values()) {
      if (ObjectUtil.equals(chartType.getDescription(), description)) {
        return chartType;
      }
    }

    return null;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return getCode();
  }

}
