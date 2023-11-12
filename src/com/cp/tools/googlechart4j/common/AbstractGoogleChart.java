
package com.cp.tools.googlechart4j.common;

import static com.cp.tools.googlechart4j.util.GoogleChartUtil.PARAMETER_DELIMITER;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractGoogleChart implements GoogleChart {

  protected static final double EXPECTED_CHART_AREA = 300000.0;
  protected static final double EXPECTED_CHART_HEIGHT = 1000.0;
  protected static final double EXPECTED_CHART_WIDTH = 1000.0;

  protected static final String BASE_URL_STRING = "http://chart.apis.google.com/chart?";

  protected final Log logger = LogFactory.getLog(getClass());

  private Dimension size;

  private GoogleChartType type;

  private String title;

  // Package-private constructor used only for testing purposes!
  AbstractGoogleChart() {
  }

  public AbstractGoogleChart(final GoogleChartType type, final Dimension size) {
    setType(type);
    setSize(size);
  }

  protected double getExpectedArea() {
    return EXPECTED_CHART_AREA;
  }

  protected double getExpectedHeight() {
    return EXPECTED_CHART_HEIGHT;
  }

  protected double getExpectedWidth() {
    return EXPECTED_CHART_WIDTH;
  }

  protected int getHeight() {
    return (int) getSize().getHeight();
  }

  public final Dimension getSize() {
    Assert.state(ObjectUtil.isNotNull(size), "The size of the Google Chart/Map has not been specified!");
    return size;
  }

  private void setSize(final Dimension size) {
    assertSize(size);
    this.size = size;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public final GoogleChartType getType() {
    Assert.state(ObjectUtil.isNotNull(type), "The type of Google Chart/Map has not been specified!");
    return type;
  }

  private void setType(final GoogleChartType type) {
    Assert.notNull(type, "The type of Google Chart/Map cannot be null!");
    this.type = type;
  }

  protected int getWidth() {
    return (int) getSize().getWidth();
  }

  protected void assertSize(final Dimension size) {
    Assert.notNull(size, "The size of the Google Chart/Map cannot be null!");

    final double actualHeight = size.getHeight();
    final double actualWidth = size.getWidth();

    Assert.greaterThanEqual(actualHeight, 0.0, "The actual height (" + actualHeight
      + ") of the Google Chart/Map must be greater than or equal to 0!");

    Assert.lessThanEqual(actualHeight, getExpectedHeight(), "The actual height (" + actualHeight
      + ") of the Google Chart/Map has to be less than or equal to (" + getExpectedHeight() + ")!");

    Assert.greaterThanEqual(actualWidth, 0.0, "The actual width (" + actualWidth
      + ") of the Google Chart/Map must be greater than or equal to 0!");

    Assert.lessThanEqual(actualWidth, getExpectedWidth(), "The actual width (" + actualWidth
      + ") of the Google Chart/Map has to be less than or equal to (" + getExpectedWidth() + ")!");

    final double actualArea = (size.getWidth() * size.getHeight());

    Assert.lessThanEqual(actualArea, getExpectedArea(), "The actual area (" + actualArea
      + ") of the Google Chart/Map must be less than or equal to (" + getExpectedArea() + ")!");
  }

  protected String encodeSize() {
    final StringBuffer buffer = new StringBuffer(PARAMETER_DELIMITER);
    buffer.append("chs=");
    buffer.append(getWidth());
    buffer.append("x");
    buffer.append(getHeight());
    return buffer.toString();
  }

  protected String encodeTitle() {
    if (StringUtil.isNotEmpty(getTitle())) {
      final StringBuffer buffer = new StringBuffer(PARAMETER_DELIMITER);
      buffer.append("chtt=");
      buffer.append(getTitle());
      return buffer.toString();
    }
    else {
      return "";
    }
  }

  protected String encodeType() {
    final StringBuffer buffer = new StringBuffer("cht=");
    buffer.append(getType().getCode());
    return buffer.toString();
  }

  protected String encodeUrl() {
    final StringBuffer url = new StringBuffer(BASE_URL_STRING);
    url.append(encodeType());
    url.append(encodeSize());
    url.append(encodeTitle());
    return url.toString();
  }

  public URL toUrl() throws MalformedURLException {
    return new URL(encodeUrl());
  }

}
