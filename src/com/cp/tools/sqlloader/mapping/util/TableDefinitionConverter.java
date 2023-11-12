
package com.cp.tools.sqlloader.mapping.util;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.common.lang.Assert;
import com.cp.common.util.record.AbstractRecordFactory;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.RecordTable;

public final class TableDefinitionConverter {

  private TableDefinitionConverter() {
  }

  public static RecordTable toRecordTable(final TableDefinition tableDefinition) {
    return AbstractRecordFactory.getInstance().getRecordTableInstance(getColumns(tableDefinition));
  }

  public static Column[] getColumns(final TableDefinition tableDefinition) {
    Assert.notNull(tableDefinition, "The table definition cannot be null!");

    final Column[] columns = new Column[tableDefinition.getColumnDefinitionCount()];
    int index = 0;

    for (final ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitions()) {
      columns[index++] = ColumnDefinitionConverter.toColumn(columnDefinition);
    }

    return columns;
  }

}
