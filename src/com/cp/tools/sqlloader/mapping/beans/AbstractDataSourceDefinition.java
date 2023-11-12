
package com.cp.tools.sqlloader.mapping.beans;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractDataSourceDefinition implements DataSourceDefinition {

  protected final Log logger = LogFactory.getLog(getClass());

  private final List<TableDefinition> tableDefinitionList;

  private String jdbcDriver;
  private String jdbcPassword;
  private String jdbcUrl;
  private String jdbcUsername;
  private final String name;

  public AbstractDataSourceDefinition(final String name) {
    Assert.notEmpty(name, "The name of the data source cannot be null or empty!");
    this.name = name;
    this.tableDefinitionList = getTableDefinitionListImpl();
  }

  protected List<TableDefinition> getTableDefinitionListImpl() {
    return new LinkedList<TableDefinition>();
  }

  public String getJdbcDriver() {
    return jdbcDriver;
  }

  public void setJdbcDriver(final String jdbcDriver) {
    this.jdbcDriver = jdbcDriver;
  }

  public String getJdbcPassword() {
    return jdbcPassword;
  }

  public void setJdbcPassword(final String jdbcPassword) {
    this.jdbcPassword = jdbcPassword;
  }

  public String getJdbcUrl() {
    return jdbcUrl;
  }

  public void setJdbcUrl(final String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }

  public String getJdbcUsername() {
    return jdbcUsername;
  }

  public void setJdbcUsername(final String jdbcUsername) {
    this.jdbcUsername = jdbcUsername;
  }

  public final String getName() {
    return name;
  }

  public int getTableDefinitionCount() {
    return tableDefinitionList.size();
  }

  public List<TableDefinition> getTableDefinitions() {
    return Collections.unmodifiableList(tableDefinitionList);
  }

  public boolean add(final TableDefinition tableDefinition) {
    Assert.notNull(tableDefinition, "The table definition cannot be null!");
    return tableDefinitionList.add(tableDefinition);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof DataSourceDefinition)) {
      return false;
    }

    final DataSourceDefinition that = (DataSourceDefinition) obj;

    return ObjectUtil.equals(getJdbcDriver(), that.getJdbcDriver())
      && ObjectUtil.equals(getJdbcPassword(), that.getJdbcPassword())
      && ObjectUtil.equals(getJdbcUrl(), that.getJdbcUrl())
      && ObjectUtil.equals(getJdbcUsername(), that.getJdbcUsername())
      && ObjectUtil.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getJdbcDriver());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getJdbcPassword());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getJdbcUrl());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getJdbcUsername());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getName());
    return hashValue;
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{jdbcDriver = ");
    buffer.append(getJdbcDriver());
    buffer.append(", jdbcPassword = ").append(getJdbcPassword());
    buffer.append(", jdbcUrl = ").append(getJdbcUrl());
    buffer.append(", jdbcUsername = ").append(getJdbcUsername());
    buffer.append(", name = ").append(getName());
    buffer.append("}").append(getClass().getName());
    return buffer.toString();
  }

}
