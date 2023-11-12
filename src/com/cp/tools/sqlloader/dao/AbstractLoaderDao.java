
package com.cp.tools.sqlloader.dao;

import com.cp.tools.sqlloader.dao.support.ObjectTypeToJdbcTypeConverter;
import com.cp.tools.sqlloader.dao.support.ObjectTypeToSqlTypeConverter;
import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.tools.sqlloader.sql.SqlDialectFactory;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.record.RecordTable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public abstract class AbstractLoaderDao extends JdbcDaoSupport implements LoaderDao {

  protected final Log logger = LogFactory.getLog(getClass());

  private SqlDialectFactory sqlDialectFactory;

  protected BatchPreparedStatementSetter getBatchPreparedStatementSetter(final TableDefinition tableDefinition, final RecordTable data) {
    return new BatchPreparedStatementSetter() {

      public int getBatchSize() {
        return data.rowCount();
      }

      public void setValues(final PreparedStatement ps, final int rowIndex) throws SQLException {
        int columnIndex = 1;

        for (final ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitions()) {
          final Object cellValue = data.getCellValue(rowIndex, data.getColumn(columnDefinition.getProperty()));

          if (ObjectUtil.isNotNull(cellValue)) {
            ps.setObject(columnIndex++, ObjectTypeToJdbcTypeConverter.convert(columnDefinition.getType(), cellValue));
          }
          else {
            ps.setNull(columnIndex++, ObjectTypeToSqlTypeConverter.convert(columnDefinition.getType()));
          }
        }
      }
    };
  }

  protected SqlDialectFactory getSqlDialectFactory() {
    Assert.state(ObjectUtil.isNotNull(sqlDialectFactory), "The SQL dialect factory was not properly configured in the Spring Application Context for the SqlLoader application!");
    return sqlDialectFactory;
  }

  public void setSqlDialectFactory(final SqlDialectFactory sqlDialectFactory) {
    Assert.notNull(sqlDialectFactory, "The SQL dialect factory cannot be null!");
    this.sqlDialectFactory = sqlDialectFactory;
  }

}
