/**
 * SqlLoader.java (c) 2008 March 18
 *
 * The Codeprimate SqlLoader tool is a ETL application for extracting data from some user-defined data source,
 * usually a flat file, and transforming the information to be loaded into a designated data store.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.7.21
 */

package com.cp.tools.sqlloader.main;

import com.cp.tools.sqlloader.dao.LoaderDao;
import com.cp.tools.sqlloader.io.parser.InputParser;
import com.cp.tools.sqlloader.io.parser.InputParserFactory;
import com.cp.tools.sqlloader.mapping.beans.DataSourceDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.tools.sqlloader.mapping.parser.MappingParser;
import com.cp.tools.sqlloader.sql.DelegatingDataSource;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ArrayUtil;
import com.cp.common.util.record.RecordTable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public final class SqlLoader {

  private static final Log logger = LogFactory.getLog(SqlLoader.class);

  private static final String DEFAULT_APPLICATION_CONFIGURATION_FILE = "/etc/config/sqlLoaderApplicationContext.xml";
  private static final String INPUT_PARSER_FACTORY_BEAN_NAME = "inputParserFactory";
  private static final String LOADER_DAO_BEAN_NAME = "loaderDao";
  private static final String LOG_CONFIGURATION_FILE = "/etc/config/log4j.properties";
  private static final String MAPPING_PARSER_BEAN_NAME = "mappingParser";

  private ConfigurableApplicationContext applicationContext;

  public SqlLoader() {
    initLogging();
    initApplicationContext();
  }

  protected ApplicationContext getApplicationContext() {
    Assert.state(ObjectUtil.isNotNull(applicationContext), "The Spring Application Context for the SqlLoader application was not properly initialized!");
    return applicationContext;
  }

  protected String[] getConfigLocations() {
    return new String[] { DEFAULT_APPLICATION_CONFIGURATION_FILE };
  }

  protected InputParser getInputParser(final File inputFile) {
    return getInputParserFactory().getInputParser(inputFile);
  }

  private InputParserFactory getInputParserFactory() {
    return (InputParserFactory) getApplicationContext().getBean(INPUT_PARSER_FACTORY_BEAN_NAME);
  }

  protected LoaderDao getLoaderDao(final DataSourceDefinition dataSourceDefinition) {
    final LoaderDao dao = (LoaderDao) getApplicationContext().getBean(LOADER_DAO_BEAN_NAME);

    if (dao.getDataSource() instanceof DelegatingDataSource) {
      ((DelegatingDataSource) dao.getDataSource()).init(dataSourceDefinition);
    }

    return dao;
  }

  protected MappingParser getMappingParser() {
    return (MappingParser) getApplicationContext().getBean(MAPPING_PARSER_BEAN_NAME);
  }

  protected void initApplicationContext() {
    final String[] configLocations = getConfigLocations();

    if (logger.isDebugEnabled()) {
      logger.debug("Initializing Spring Application Context using Application Configuration Files ("
        + ArrayUtil.toString(configLocations) + ")!");
    }

    applicationContext = new ClassPathXmlApplicationContext(configLocations);
    applicationContext.registerShutdownHook();
    logger.info("Spring Application Context for SqlLoader Initialized");
  }

  protected void initLogging() {
    try {
      PropertyConfigurator.configure(new ClassPathResource(LOG_CONFIGURATION_FILE).getURL());
    }
    catch (Exception e) {
      BasicConfigurator.configure();
    }
  }

  public void load(final File mappingFile) throws Exception {
    Assert.notNull(mappingFile, "The mapping file used by the SqlLoader application cannot be null!");

    if (mappingFile.exists()) {
      final List<DataSourceDefinition> dataSourceDefinitionList = getMappingParser().parse(mappingFile);

      for (final DataSourceDefinition dataSourceDefinition : dataSourceDefinitionList) {
        final LoaderDao dao = getLoaderDao(dataSourceDefinition);

        for (final TableDefinition tableDefinition : dataSourceDefinition.getTableDefinitions()) {
          final File inputFile = tableDefinition.getSource();
          final RecordTable data = getInputParser(inputFile).parse(tableDefinition, inputFile);

          dao.persist(tableDefinition, data);
        }
      }
    }
    else {
      logger.warn("The mapping file (" + mappingFile + ") could not be found in the file system!");
      throw new FileNotFoundException("The mapping file (" + mappingFile + ") could not be found in the file system!");
    }
  }

  public static void main(final String... args) throws Exception {
    if (ArrayUtil.isEmpty(args)) {
      logger.error("> java com.cp.tools.sqlloader.main.SqlLoader <absolute file system path to mapping.xml file>");
      System.exit(-1);
    }

    new SqlLoader().load(new File(args[0]));
  }

}
