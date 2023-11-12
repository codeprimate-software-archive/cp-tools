
package com.cp.tools.googlechart4j.pie;

import java.awt.Dimension;

public abstract class Abstract3DPieChart extends AbstractPieChart {

  public Abstract3DPieChart(final Dimension size) {
    super(size);
  }

  @Override
  protected void assertSize(final Dimension size) {
    super.assertSize(size);

    final double actualHeight = size.getHeight();
    final double actualWidth = size.getWidth();

    if (actualWidth < (2.5 * actualHeight)) {
      logger.warn("The recommended dimension of a 3D Pie Chart should be two and a half times wider (" + actualWidth
        + ") than it is high (" + actualHeight + ")!");
    }
  }

}
