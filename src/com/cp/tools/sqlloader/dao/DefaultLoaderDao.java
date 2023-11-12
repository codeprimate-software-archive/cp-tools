
package com.cp.tools.sqlloader.dao;

import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.common.util.record.RecordTable;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;

public class DefaultLoaderDao extends AbstractLoaderDao {

  public void persist(final TableDefinition tableDefinition, final RecordTable data) {
    final String insertSql = getSqlDialectFactory().createInsertSql(tableDefinition);
    final String parsedInsertSql = NamedParameterUtils.parseSqlStatementIntoString(insertSql);

    if (logger.isDebugEnabled()) {
      logger.debug("insert SQL (" + insertSql + ")");
      logger.debug("parsed insert SQL (" + parsedInsertSql + ")");
    }

    getJdbcTemplate().batchUpdate(parsedInsertSql, getBatchPreparedStatementSetter(tableDefinition, data));
  }

}
