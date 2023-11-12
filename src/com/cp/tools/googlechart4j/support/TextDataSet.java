
package com.cp.tools.googlechart4j.support;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.text.NumberFormat;

import static com.cp.tools.googlechart4j.util.GoogleChartUtil.PARAMETER_DELIMITER;

public class TextDataSet extends AbstractDataSet {

  private static final NumberFormat NUM_FORMAT = NumberFormat.getIntegerInstance();

  private static final Scale DEFAULT_SCALE = new Scale(0, 100);

  private static final String DELIMITER = ",";

  static {
    NUM_FORMAT.setMaximumFractionDigits(1);
    NUM_FORMAT.setMinimumFractionDigits(1);
    NUM_FORMAT.setMinimumIntegerDigits(1);
  }

  private final float[] values;

  private final Scale scale;

  public TextDataSet(final float... values) {
    this(DEFAULT_SCALE, values);
  }

  public TextDataSet(final Scale scale, final float... values) {
    super(DataEncoding.TEXT);
    this.scale = ObjectUtil.getDefaultValue(scale, DEFAULT_SCALE);
    assertValues(this.scale, values);
    this.values = values;
  }

  public Scale getScale() {
    return scale;
  }

  public float[] getValues() {
    return values;
  }

  protected void assertValues(final Scale scale, final float... values) {
    Assert.notNull(values, "The array of floating point numbers defining the chart data cannot be null!");
    Assert.isTrue(values.length > 0, "The array of floating point numbers defining the chart data cannot be empty!");

    for (final float value : values) {
      Assert.greaterThanEqual(new Float(value), new Float(scale.getMinValue()), "The floating point value (" + value
        + ") must be greater or equal to (" + scale.getMinValue() + ")!");
      Assert.lessThanEqual(new Float(value), new Float(scale.getMaxValue()), "The floating point value (" + value
        + ") must be less than or equal to (" + scale.getMaxValue() + ")!");
    }
  }

  public String encode() {
    final StringBuffer buffer = new StringBuffer("chd=");
    buffer.append(getEncoding().getCode());
    buffer.append(":");
    buffer.append(toString());
    buffer.append(PARAMETER_DELIMITER);
    buffer.append("chds=").append(getScale().encode());
    return buffer.toString();
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    boolean flag = false;

    for (final float value : getValues()) {
      buffer.append(flag ? DELIMITER : "");
      buffer.append(NUM_FORMAT.format(value));
      flag = true;
    }

    return buffer.toString();
  }

}
