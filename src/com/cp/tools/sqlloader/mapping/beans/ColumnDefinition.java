
package com.cp.tools.sqlloader.mapping.beans;

import java.util.List;

public interface ColumnDefinition {

  public boolean isConstrained();

  public int getConstraintDefinitionCount();

  public List<ConstraintDefinition> getConstraintDefinitions();

  public int getEndIndex();

  public void setEndIndex(int endIndex);

  public String getName();

  public String getProperty();

  public void setProperty(String property);

  public int getStartIndex();

  public void setStartIndex(int startIndex);

  public Class getType();

  public void setType(Class type);

  public void setType(String fullyQualifiedClassName) throws ClassNotFoundException;

  public boolean add(ConstraintDefinition constraint);

}
