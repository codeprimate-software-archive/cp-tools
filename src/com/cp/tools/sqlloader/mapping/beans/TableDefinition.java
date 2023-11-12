
package com.cp.tools.sqlloader.mapping.beans;

import java.io.File;
import java.util.List;

public interface TableDefinition {

  public ColumnDefinition getColumnDefinitionAtIndex(int index);

  public ColumnDefinition getColumnDefinitionByName(String name);

  public int getColumnDefinitionCount();

  public List<ColumnDefinition> getColumnDefinitions();

  public String getName();

  public File getSource();

  public void setSource(File inputFile);

  public boolean add(ColumnDefinition column);

}
