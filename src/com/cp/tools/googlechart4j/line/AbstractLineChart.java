
package com.cp.tools.googlechart4j.line;

import com.cp.tools.googlechart4j.common.GoogleChartType;
import java.awt.Dimension;

public abstract class AbstractLineChart extends com.cp.tools.googlechart4j.common.AbstractGoogleChart implements LineChart {

  public AbstractLineChart(final Dimension size) {
    super(GoogleChartType.LINE_CHART, size);
  }

}
