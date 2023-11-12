
package com.cp.tools.sqlloader.mapping.beans;

public final class MappingBeanFactory {

  private MappingBeanFactory() {
  }

  public static ColumnDefinition getColumnDefinition(final String name) {
    return new DefaultColumnDefinition(name);
  }

  public static ConstraintDefinition getConstraintDefinition(final String name) {
    return new DefaultConstraintDefinition(name);
  }

  public static DataSourceDefinition getDataSourceDefinition(final String name) {
    return new DefaultDataSourceDefinition(name);
  }

  public static TableDefinition getTableDefinition(final String name) {
    return new DefaultTableDefinition(name);
  }

}
