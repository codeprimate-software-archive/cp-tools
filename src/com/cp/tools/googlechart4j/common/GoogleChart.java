
package com.cp.tools.googlechart4j.common;

import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;

public interface GoogleChart {

  public Object getData();

  public void setData(Object data);

  public Dimension getSize();

  public String getTitle();

  public void setTitle(String title);

  public GoogleChartType getType();

  public URL toUrl() throws MalformedURLException;

}
