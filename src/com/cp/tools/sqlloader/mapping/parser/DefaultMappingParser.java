
package com.cp.tools.sqlloader.mapping.parser;

import com.cp.tools.sqlloader.common.parser.ParserUtil;
import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.ConstraintDefinition;
import com.cp.tools.sqlloader.mapping.beans.DataSourceDefinition;
import com.cp.tools.sqlloader.mapping.beans.MappingBeanFactory;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.SystemException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.Assert;

public class DefaultMappingParser extends AbstractMappingParser {

  public List<DataSourceDefinition> parse(final File mappingFile) throws FileNotFoundException {
    Assert.notNull(mappingFile, "The mapping file cannot be null!");

    if (mappingFile.exists()) {
      try {
        final SAXReader reader = new SAXReader();
        final Document mappingDocument = reader.read(mappingFile);
        final List<DataSourceDefinition> dataSourceDefinitionList = new LinkedList<DataSourceDefinition>();

        for (final Iterator it = mappingDocument.getRootElement().elementIterator(DATASOURCE_TAG); it.hasNext(); ) {
          dataSourceDefinitionList.add(parseDataSourceElement((Element) it.next()));
        }

        return dataSourceDefinitionList;
      }
      catch (DocumentException e) {
        logger.error("Failed to read the contents of the SqlLoader mapping XML document (" + mappingFile + ")!", e);
        throw new SystemException("Failed to read the contents of the SqlLoader mapping XML document ("
          + mappingFile + ")!", e);
      }
    }
    else {
      logger.warn("The SqlLoader mapping file (" + mappingFile + ") was not found in the filesystem!");
      throw new FileNotFoundException("The SqlLoader mapping file (" + mappingFile + ") was not found in the filesystem!");
    }
  }

  protected DataSourceDefinition parseDataSourceElement(final Element dataSourceElement) {
    final String jdbcDriverAttribute = dataSourceElement.attributeValue(JDBC_DRIVER_ATTRIBUTE);
    final String jdbcPasswordAttribute = dataSourceElement.attributeValue(JDBC_PASSWORD_ATTRIBUTE);
    final String jdbcUrlAttribute = dataSourceElement.attributeValue(JDBC_URL_ATTRIBUTE);
    final String jdbcUsernameAttribute = dataSourceElement.attributeValue(JDBC_USERNAME_ATTRIBUTE);
    final String nameAttribute = dataSourceElement.attributeValue(NAME_ATTRIBUTE);

    final DataSourceDefinition dataSourceDefinition = MappingBeanFactory.getDataSourceDefinition(nameAttribute);
    dataSourceDefinition.setJdbcDriver(jdbcDriverAttribute);
    dataSourceDefinition.setJdbcPassword(jdbcPasswordAttribute);
    dataSourceDefinition.setJdbcUrl(jdbcUrlAttribute);
    dataSourceDefinition.setJdbcUsername(jdbcUsernameAttribute);

    for (final Iterator it = dataSourceElement.elementIterator(TABLE_TAG); it.hasNext(); ) {
      dataSourceDefinition.add(parseTableElement((Element) it.next()));
    }

    return dataSourceDefinition;
  }

  protected TableDefinition parseTableElement(final Element tableElement) {
    final String nameAttribute = tableElement.attributeValue(NAME_ATTRIBUTE);
    final String sourceAttribute = tableElement.attributeValue(SOURCE_ATTRIBUTE);

    final TableDefinition tableDefinition = MappingBeanFactory.getTableDefinition(nameAttribute);

    if (StringUtil.isNotEmpty(sourceAttribute)) {
      tableDefinition.setSource(new File(sourceAttribute));
    }

    for (final Iterator it = tableElement.elementIterator(COLUMN_TAG); it.hasNext(); ) {
      tableDefinition.add(parseColumnElement((Element) it.next()));
    }

    return tableDefinition;
  }

  protected  ColumnDefinition parseColumnElement(final Element columnElement) {
    final String endIndexAttribute = columnElement.attributeValue(END_INDEX_ATTRIBUTE);
    final String nameAttribute = columnElement.attributeValue(NAME_ATTRIBUTE);
    final String propertyAttribute = columnElement.attributeValue(PROPERTY_ATTRIBUTE);
    final String startIndexAttribute = columnElement.attributeValue(START_INDEX_ATTRIBUTE);
    final String typeAttribute = columnElement.attributeValue(TYPE_ATTRIBUTE);

    try {
      final ColumnDefinition columnDefinition = MappingBeanFactory.getColumnDefinition(nameAttribute);
      columnDefinition.setEndIndex(ParserUtil.parseInt(endIndexAttribute));
      columnDefinition.setProperty(propertyAttribute);
      columnDefinition.setStartIndex(ParserUtil.parseInt(startIndexAttribute));
      columnDefinition.setType(typeAttribute);

      for (final Iterator it = columnElement.elementIterator(CONSTRAINT_TAG); it.hasNext(); ) {
        columnDefinition.add(parseConstraintElement((Element) it.next()));
      }

      return columnDefinition;
    }
    catch (ClassNotFoundException e) {
      logger.error("The type (" + typeAttribute + ") specified in column (" + nameAttribute
        + ") could not be found in the classpath!", e);
      throw new SystemException("The type (" + typeAttribute + ") specified in column (" + nameAttribute
        + ") could not be found in the classpath!", e);
    }
  }

  protected ConstraintDefinition parseConstraintElement(final Element constraintElement) {
    final String maxAttribute = constraintElement.attributeValue(MAX_ATTRIBUTE);
    final String minAttribute = constraintElement.attributeValue(MIN_ATTRIBUTE);
    final String nameAttribute = constraintElement.attributeValue(NAME_ATTRIBUTE);
    final String patternAttribute = constraintElement.attributeValue(PATTERN_ATTRIBUTE);

    final ConstraintDefinition constraintDefinition = MappingBeanFactory.getConstraintDefinition(nameAttribute);
    constraintDefinition.setMax(maxAttribute);
    constraintDefinition.setMin(minAttribute);
    constraintDefinition.setPattern(patternAttribute);

    return constraintDefinition;
  }

}
