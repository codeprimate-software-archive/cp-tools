
package com.cp.tools.sqlloader.mapping.beans;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractTableDefinition implements TableDefinition {

  protected final Log logger = LogFactory.getLog(getClass());

  private final List<ColumnDefinition> columnDefinitionList;

  private File inputFile;

  private final String name;

  public AbstractTableDefinition(final String name) {
    Assert.notEmpty(name, "The name of the table cannot be null or empty!");
    this.name = name;
    this.columnDefinitionList = getColumnDefinitionListImpl();
  }

  public ColumnDefinition getColumnDefinitionAtIndex(final int index) {
    return columnDefinitionList.get(index);
  }

  public ColumnDefinition getColumnDefinitionByName(final String name) {
    for (final ColumnDefinition columnDefinition : columnDefinitionList) {
      if (ObjectUtil.equals(columnDefinition.getName(), name)) {
        return columnDefinition;
      }
    }

    return null;
  }

  public int getColumnDefinitionCount() {
    return columnDefinitionList.size();
  }

  protected List<ColumnDefinition> getColumnDefinitionListImpl() {
    return new LinkedList<ColumnDefinition>();
  }

  public List<ColumnDefinition> getColumnDefinitions() {
    return Collections.unmodifiableList(columnDefinitionList);
  }

  public final String getName() {
    return name;
  }

  public File getSource() {
    return inputFile;
  }

  public void setSource(final File inputFile) {
    this.inputFile = inputFile;
  }

  public boolean add(final ColumnDefinition column) {
    Assert.notNull(column, "The column entry cannot be null!");
    return columnDefinitionList.add(column);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof TableDefinition)) {
      return false;
    }

    final TableDefinition that = (TableDefinition) obj;

    return ObjectUtil.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getName());
    return hashValue;
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer(getName());
    String delimiter = "";

    buffer.append(" (");

    for (final ColumnDefinition column : getColumnDefinitions()) {
      buffer.append(delimiter).append(column.getName());
      delimiter = ", ";
    }

    buffer.append(")");

    return buffer.toString();
  }

}
