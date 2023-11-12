
package com.cp.tools.googlechart4j.support;

import com.cp.common.lang.ObjectUtil;

public final class Scale {

  private static final String DELIMITER = ",";

  private final int maxValue;
  private final int minValue;

  public Scale(final int minValue, final int maxValue) {
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  public int getMaxValue() {
    return maxValue;
  }

  public int getMinValue() {
    return minValue;
  }

  public String encode() {
    final StringBuffer buffer = new StringBuffer(String.valueOf(getMinValue()));
    buffer.append(DELIMITER);
    buffer.append(String.valueOf(getMaxValue()));
    return buffer.toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Scale)) {
      return false;
    }

    final Scale that = (Scale) obj;

    return ObjectUtil.equals(getMinValue(), that.getMinValue())
      && ObjectUtil.equals(getMaxValue(), that.getMaxValue());
  }

  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getMinValue());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getMaxValue());
    return hashValue;
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("[");
    buffer.append(getMinValue());
    buffer.append("-");
    buffer.append(getMaxValue());
    buffer.append("]");
    return buffer.toString();
  }

}
