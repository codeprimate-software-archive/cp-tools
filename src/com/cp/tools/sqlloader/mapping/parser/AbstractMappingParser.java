
package com.cp.tools.sqlloader.mapping.parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractMappingParser implements MappingParser {

  // valid tags in the sqlloader-mapping.xml file
  protected static final String COLUMN_TAG = "column";
  protected static final String CONSTRAINT_TAG = "constraint";
  protected static final String DATASOURCE_TAG = "datasource";
  protected static final String TABLE_TAG = "table";

  // valid attributes of tags in the sqlloader-mapping.xml file
  protected static final String END_INDEX_ATTRIBUTE = "endIndex";
  protected static final String JDBC_DRIVER_ATTRIBUTE = "jdbcDriver";
  protected static final String JDBC_PASSWORD_ATTRIBUTE = "jdbcPassword";
  protected static final String JDBC_URL_ATTRIBUTE = "jdbcUrl";
  protected static final String JDBC_USERNAME_ATTRIBUTE = "jdbcUsername";
  protected static final String MAX_ATTRIBUTE = "max";
  protected static final String MIN_ATTRIBUTE = "min";
  protected static final String NAME_ATTRIBUTE = "name";
  protected static final String PATTERN_ATTRIBUTE = "pattern";
  protected static final String PROPERTY_ATTRIBUTE = "property";
  protected static final String SOURCE_ATTRIBUTE = "source";
  protected static final String START_INDEX_ATTRIBUTE = "startIndex";
  protected static final String TYPE_ATTRIBUTE = "type";

  protected final Log logger = LogFactory.getLog(getClass());

}
