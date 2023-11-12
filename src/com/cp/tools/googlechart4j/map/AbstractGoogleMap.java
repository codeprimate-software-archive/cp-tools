
package com.cp.tools.googlechart4j.map;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.awt.Dimension;

public abstract class AbstractGoogleMap extends com.cp.tools.googlechart4j.common.AbstractGoogleChart implements GoogleMap {

  protected static final double EXPECTED_MAP_HEIGHT = 220.0;
  protected static final double EXPECTED_MAP_WIDTH = 440.0;
  protected static final double EXPECTED_MAP_AREA = (EXPECTED_MAP_WIDTH * EXPECTED_MAP_HEIGHT);

  private GeographicalArea geographicalArea;

  public AbstractGoogleMap(final Dimension size) {
    super(com.cp.tools.googlechart4j.common.GoogleChartType.MAP, size);
  }

  @Override
  protected double getExpectedArea() {
    return EXPECTED_MAP_AREA;
  }

  @Override
  protected double getExpectedHeight() {
    return EXPECTED_MAP_HEIGHT;
  }

  @Override
  protected double getExpectedWidth() {
    return EXPECTED_MAP_WIDTH;
  }

  public GeographicalArea getGeographicalArea() {
    Assert.state(ObjectUtil.isNotNull(geographicalArea), "The geographical area of the Google Map has not been specified!");
    return geographicalArea;
  }

  public void setGeographicalArea(final GeographicalArea geographicalArea) {
    Assert.notNull(geographicalArea, "The geographical area of the Google Map cannot be null!");
    this.geographicalArea = geographicalArea;
  }

}
