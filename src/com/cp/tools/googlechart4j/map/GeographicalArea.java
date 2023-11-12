
package com.cp.tools.googlechart4j.map;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;

public enum GeographicalArea {
  AFRICA("africa", "Africa"),
  ASIA("asia", "Asia"),
  EUROPE("europe", "Europe"),
  MIDDLE_EAST("middle_east", "Middle East"),
  SOUTH_AMERICA("south_america", "South America"),
  USA("usa", "United States of America"),
  WORLD("world", "World");

  private final String code;
  private final String description;

  GeographicalArea(final String code, final String description) {
    Assert.notEmpty(code, "The code specifying the Geographical Area cannot be null or empty!");
    Assert.notEmpty(description, "The description of the Geographical Area cannot be null or empty!");
    this.code = code;
    this.description = description;
  }

  public static GeographicalArea getGeographicalAreaByCode(final String code) {
    for (final GeographicalArea area : values()) {
      if (ObjectUtil.equals(area.getCode(), code)) {
        return area;
      }
    }

    return null;
  }

  public static GeographicalArea getGeographicalAreaByDescription(final String description) {
    for (final GeographicalArea area : values()) {
      if (ObjectUtil.equals(area.getDescription(), description)) {
        return area;
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
