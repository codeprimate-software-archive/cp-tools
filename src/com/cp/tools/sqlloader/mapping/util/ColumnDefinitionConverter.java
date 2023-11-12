
package com.cp.tools.sqlloader.mapping.util;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.ColumnImpl;

public final class ColumnDefinitionConverter {

  private ColumnDefinitionConverter() {
  }

  public static Column toColumn(final ColumnDefinition columnDefinition) {
    return new ColumnImpl(columnDefinition.getProperty(), columnDefinition.getType());
  }

}
