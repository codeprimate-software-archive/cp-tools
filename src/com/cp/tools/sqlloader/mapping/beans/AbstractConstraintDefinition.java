
package com.cp.tools.sqlloader.mapping.beans;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractConstraintDefinition implements ConstraintDefinition {

  protected final Log logger = LogFactory.getLog(getClass());

  private String format;
  private String max;
  private String min;
  private final String name;

  public AbstractConstraintDefinition(final String name) {
    Assert.notEmpty(name, "The name of the constraint cannot be null or empty!");
    this.name = name;
  }

  public String getMax() {
    return max;
  }

  public void setMax(final String max) {
    this.max = max;
  }

  public String getMin() {
    return min;
  }

  public void setMin(final String min) {
    this.min = min;
  }

  public final String getName() {
    return name;
  }

  public String getPattern() {
    return format;
  }

  public void setPattern(final String format) {
    this.format = format;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof ConstraintDefinition)) {
      return false;
    }

    final ConstraintDefinition that = (ConstraintDefinition) obj;

    return ObjectUtil.equals(getMax(), that.getMax())
      && ObjectUtil.equals(getMin(), that.getMin())
      && ObjectUtil.equals(getName(), that.getName())
      && ObjectUtil.equals(getPattern(), that.getPattern());
  }

  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getMax());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getMin());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getName());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getPattern());
    return hashValue;
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{max = ");
    buffer.append(getMax());
    buffer.append(", min = ").append(getMin());
    buffer.append(", name = ").append(getName());
    buffer.append(", pattern = ").append(getPattern());
    buffer.append("}").append(getClass().getName());
    return buffer.toString();
  }

}
