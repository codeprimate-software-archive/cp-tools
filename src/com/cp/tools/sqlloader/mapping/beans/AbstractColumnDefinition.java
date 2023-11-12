
package com.cp.tools.sqlloader.mapping.beans;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractColumnDefinition implements ColumnDefinition {

  protected final Log logger = LogFactory.getLog(getClass());

  private int endIndex = -1;
  private int startIndex = -1;

  private Class type;

  private final List<ConstraintDefinition> constraintDefinitionList;

  private final String name;
  private String property;

  public AbstractColumnDefinition(final String name) {
    Assert.notEmpty(name, "The name of the column cannot be null or empty!");
    this.name = name;
    this.constraintDefinitionList = getConstraintDefinitionListImpl();
  }

  public boolean isConstrained() {
    return !constraintDefinitionList.isEmpty();
  }

  public int getConstraintDefinitionCount() {
    return constraintDefinitionList.size();
  }

  protected List<ConstraintDefinition> getConstraintDefinitionListImpl() {
    return new LinkedList<ConstraintDefinition>();
  }

  public List<ConstraintDefinition> getConstraintDefinitions() {
    return Collections.unmodifiableList(constraintDefinitionList);
  }

  public int getEndIndex() {
    return endIndex;
  }

  public void setEndIndex(final int endIndex) {
    this.endIndex = endIndex;
  }

  public final String getName() {
    return name;
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(final String property) {
    this.property = property;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public void setStartIndex(final int startIndex) {
    this.startIndex = startIndex;
  }

  public Class getType() {
    return type;
  }

  public void setType(final Class type) {
    this.type = type;
  }

  public void setType(final String fullyQualifiedClassName) throws ClassNotFoundException {
    setType(ClassUtil.getClass(fullyQualifiedClassName));
  }

  public boolean add(final ConstraintDefinition constraint) {
    Assert.notNull(constraint, "The constraint entry cannot be null!");
    return constraintDefinitionList.add(constraint);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof ColumnDefinition)) {
      return false;
    }

    final ColumnDefinition that = (ColumnDefinition) obj;

    return ObjectUtil.equals(getEndIndex(), that.getEndIndex())
      && ObjectUtil.equals(getName(), that.getName())
      && ObjectUtil.equals(getProperty(), that.getProperty())
      && ObjectUtil.equals(getStartIndex(), that.getStartIndex())
      && ObjectUtil.equals(getType(), that.getType());
  }

  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getEndIndex());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getName());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getProperty());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getStartIndex());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getType());
    return hashValue;
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{endIndex = ");
    buffer.append(getEndIndex());
    buffer.append(", name = ").append(getName());
    buffer.append(", property = ").append(getProperty());
    buffer.append(", startIndex = ").append(getStartIndex());
    buffer.append(", type = ").append(getType());
    buffer.append("}").append(getClass().getName());
    return buffer.toString();
  }

}
