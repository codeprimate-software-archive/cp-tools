
package com.cp.tools.googlechart4j.pie;

import com.cp.tools.googlechart4j.common.AbstractGoogleChart;
import com.cp.tools.googlechart4j.common.GoogleChartType;
import java.awt.Dimension;

public abstract class AbstractPieChart extends AbstractGoogleChart {

  public AbstractPieChart(final Dimension size) {
    super(GoogleChartType.PIE_CHART, size);
  }

  @Override
  protected void assertSize(final Dimension size) {
    super.assertSize(size);

    final double actualHeight = size.getHeight();
    final double actualWidth = size.getWidth();

    if (actualWidth < (2.0 * actualHeight)) {
      logger.warn("The recommended dimension of a Pie Chart should be twice as wide (" + actualWidth
        + ") as it is high (" + actualHeight + ")!");
    }
  }

}
