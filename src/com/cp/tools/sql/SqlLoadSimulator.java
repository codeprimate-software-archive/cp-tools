/*
 * Copyright (c) 2010. Codeprimate, LLC and original author(s).  All Rights Reserved.
 *
 * This software is licensed under the Codeprimate License.  Unless required by applicable law or agreed to in writing, the software is
 * distributed and provided "as is" without any expressed or implied WARRANTIES or CONDITIONS of any kind.  By using the software,
 * the end-user agrees to all terms and conditions of the license.
 */

package com.cp.tools.sql;

import com.cp.common.context.config.Config;
import com.cp.common.context.config.PropertiesConfig;
import com.cp.common.io.IoUtil;
import com.cp.common.lang.Assert;
import com.cp.common.lang.Destroyable;
import com.cp.common.lang.Interruptable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.lang.Visitable;
import com.cp.common.lang.support.InterruptVisitor;
import com.cp.common.sql.AbstractDataSource;
import com.cp.common.sql.JdbcUtil;
import com.cp.common.util.CollectionUtil;
import com.cp.common.util.SystemException;
import com.cp.common.util.Visitor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;
import javax.sql.DataSource;

import static com.cp.common.lang.UtilityMethods.is;

/**
 * The SqlLoadSimulator class...
 * <p/>
 * SqlLoaderTest.java (c) 1 December 2010
 * @author John Blum
 * @version $Revision: 1.3 $
 * @see com.cp.common.context.config.Config
 * @see com.cp.common.io.IoUtil
 * @see com.cp.common.lang.Destroyable
 * @see com.cp.common.lang.Interruptable
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.lang.support.InterruptVisitor
 * @see com.cp.common.sql.AbstractDataSource
 * @see com.cp.common.sql.JdbcUtil
 * @see com.cp.common.util.Visitor
 * @see java.lang.Runnable
 * @see java.sql.Connection
 * @see java.sql.DriverManager
 * @see java.sql.PreparedStatement
 * @see java.sql.Types
 * @see java.util.Properties
 * @see java.util.Random
 * @see java.util.concurrent.CountDownLatch
 * @see java.util.concurrent.ExecutorService
 * @see java.util.concurrent.Executors
 * @see java.util.concurrent.locks.LockSupport
 * @see javax.sql.DataSource
 */
public class SqlLoadSimulator implements Destroyable, Runnable {

  protected static final boolean DEFAULT_FAIL_FOR_MISSING_PARAMETERS = true;

  protected static final int DEFAULT_CONNECTION_VALIDATION_TIMEOUT_IN_SECONDS = 5;
  protected static final int DEFAULT_SESSION_ADD_COUNT = 10;
  protected static final int DEFAULT_SESSION_ADD_INTERVAL = 15;
  protected static final int DEFAULT_SESSION_BEGIN_COUNT = 10;
  protected static final int DEFAULT_SESSION_END_COUNT = 100;
  protected static final int DEFAULT_SQL_TRANSACTION_COMMIT_COUNT = 100;
  protected static final int DEFAULT_WAIT_ATTEMPTS = 3;

  protected static final Map<String, Integer> SQL_TYPE_MAP_BY_NAME = new HashMap<String, Integer>(11);

  protected static final String DEFAULT_CONFIGURATION_FILE = "/etc/config/sqlloadsimulator.properties";
  protected static final String DEFAULT_JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
  protected static final String DEFAULT_SESSION_ADD_INTERVAL_UNIT = "second";
  protected static final String DEFAULT_SQL_PARAMETER_FILE_ARGUMENT_DELIMITER = ",";

  static {
    SQL_TYPE_MAP_BY_NAME.put("CHAR", Types.CHAR);
    SQL_TYPE_MAP_BY_NAME.put("DATE", Types.DATE);
    SQL_TYPE_MAP_BY_NAME.put("NUMERIC", Types.NUMERIC);
    SQL_TYPE_MAP_BY_NAME.put("TIMESTAMP", Types.TIMESTAMP);
    SQL_TYPE_MAP_BY_NAME.put("VARCHAR", Types.VARCHAR);
  }

  private boolean destroyed = false;

  private Collection<Metric> metrics;

  private final Config applicationConfiguration;

  private CountDownLatch countDownLock;

  private final DataSource dataSource;

  private final ExecutorService executorService;

  private final List<SqlParameterSource> sqlParameters;
  private final List<SqlRunner> workers;

  private final Random randomNumberGenerator;

  /**
   * Constructs an instance of the SqlLoadSimulator class initialized to the user configuration file at the specified
   * location.
   * <p/>
   * @param userConfigurationFileLocation a String value specifying the location of the user configuration file for the
   * SqlLoadSimulator command-line application.
   */
  public SqlLoadSimulator(final String userConfigurationFileLocation) {
    applicationConfiguration = new PropertiesConfig(initApplicationConfiguration(userConfigurationFileLocation));
    dataSource = initDataSource();
    executorService = initExecutorService();
    sqlParameters = new ArrayList<SqlParameterSource>(loadSqlParameters());
    metrics = Collections.synchronizedList(new LinkedList<Metric>());
    randomNumberGenerator = new Random(System.currentTimeMillis());
    workers = new LinkedList<SqlRunner>();
  }

  protected Properties initApplicationConfiguration(final String userConfigurationFileLocation) {
    try {
      Properties applicationConfiguration = null;

      final Properties defaultApplicationConfiguration = new Properties();
      defaultApplicationConfiguration.load(SqlLoadSimulator.class.getResourceAsStream(DEFAULT_CONFIGURATION_FILE));
      applicationConfiguration = defaultApplicationConfiguration;

      if (StringUtil.isNotBlank(userConfigurationFileLocation)) {
        final File userConfigurationFile = new File(userConfigurationFileLocation);

        if (userConfigurationFile.isFile()) {
          final Properties userConfiguration = new Properties(defaultApplicationConfiguration);
          userConfiguration.load(new FileInputStream(userConfigurationFile));
          applicationConfiguration = userConfiguration;
          debug("Found user-specified configuration file ({0}).", userConfigurationFileLocation);
        }
        else {
          debug("The user-specified configuration file ({0}) could not be found!", userConfigurationFileLocation);
        }
      }

      return applicationConfiguration;
    }
    catch (IOException e) {
      throw new SystemException("Failed to load application configuration properties!", e);
    }
  }

  private DataSource initDataSource() {
    JdbcUtil.loadJdbcDriver(getConfig().getStringPropertyValue("sqlloadsimulator.jdbc.driver", DEFAULT_JDBC_DRIVER));

    final String jdbcUrl = getConfig().getStringPropertyValue("sqlloadsimulator.jdbc.url", DEFAULT_FAIL_FOR_MISSING_PARAMETERS);
    final String jdbcUsername = getConfig().getStringPropertyValue("sqlloadsimulator.jdbc.username", DEFAULT_FAIL_FOR_MISSING_PARAMETERS);
    final String jdbcPassword = getConfig().getStringPropertyValue("sqlloadsimulator.jdbc.password", DEFAULT_FAIL_FOR_MISSING_PARAMETERS);

    debug("JDBC URL ({0})", jdbcUrl);
    debug("JDBC User ({0})", jdbcUsername);
    debug("JDBC Password ({0})", StringUtil.isBlank(jdbcPassword) ? "unspecified" : "*****");

    return new DriverManagerDataSource(jdbcUrl, jdbcUsername, jdbcPassword);
  }

  protected ExecutorService initExecutorService() {
    return Executors.newFixedThreadPool(getConfig().getIntegerPropertyValue("sqlloadsimulator.session.end.count",
      DEFAULT_SESSION_END_COUNT));
  }

  protected Config getConfig() {
    Assert.state(ObjectUtil.isNotNull(applicationConfiguration), "The application configuration was not properly initialized!");
    return applicationConfiguration;
  }

  protected DataSource getDataSource() {
    Assert.state(ObjectUtil.isNotNull(dataSource), "The reference to the data source was not properly initialized!");
    return dataSource;
  }

  public boolean isDestroyed() {
    return destroyed;
  }

  protected ExecutorService getExecutorService() {
    Assert.state(ObjectUtil.isNotNull(executorService), "The executor service was not properly initialized!");
    return executorService;
  }

  protected SqlParameterSource getSqlParameterSource() {
    return sqlParameters.get(randomNumberGenerator.nextInt(sqlParameters.size()));
  }

  protected List<SqlRunner> getWorkers() {
    return workers;
  }

  public void addMetric(final Metric metric) {
    metrics.add(metric);
  }

  public Map<Long, Long> computeMetrics() {
    final Map<Long, Long> metricMap = new TreeMap<Long, Long>();
    final Map<Long, List<Metric>> metricListMap = new TreeMap<Long, List<Metric>>();

    for (final Metric metric : metrics) {
      List<Metric> metricList = metricListMap.get(metric.getNumberOfSessions());

      if (ObjectUtil.isNull(metricList)) {
        metricList = new LinkedList<Metric>();
        metricListMap.put(metric.getNumberOfSessions(), metricList);
      }

      metricList.add(metric);
    }

    for (final long numberOfSessions : metricListMap.keySet()) {
      final SumAverageVisitor visitor = new SumAverageVisitor();
      CollectionUtil.visit(metricListMap.get(numberOfSessions), visitor);
      metricMap.put(numberOfSessions, visitor.getAverage());
    }

    return metricMap;
  }

  public void destroy() {
    final List<SqlRunner> workers = getWorkers();

    countDownLock = new CountDownLatch(workers.size());
    CollectionUtil.visit(workers, new InterruptVisitor()); // interrupt

    int waitAttempts = DEFAULT_WAIT_ATTEMPTS;

    while (waitAttempts > 0) {
      try {
        countDownLock.await();
        waitAttempts = 0;
      }
      catch (InterruptedException e) {
        debug("Failed to wait on count down lock having ({0}) locks remaining!", countDownLock.getCount());
        waitAttempts--;
      }
    }

    CollectionUtil.visit(workers, new DestroyVisitor()); // destroy
    getExecutorService().shutdownNow();
    destroyed = true;
  }

  protected List<SqlParameterSource> loadSqlParameters() {
    final List<SqlParameterSource> sqlParameters = new LinkedList<SqlParameterSource>();

    final String sqlParameterFileLocation = getConfig().getStringPropertyValue("sqlloadsimulator.sql.parameter.file.location",
      DEFAULT_FAIL_FOR_MISSING_PARAMETERS);
    final String sqlParameterFileArgumentDelimiter = getConfig().getStringPropertyValue("sqlloadsimulator.sql.parameter.file.argument.delimiter",
      DEFAULT_SQL_PARAMETER_FILE_ARGUMENT_DELIMITER);

    Assert.notBlank(sqlParameterFileLocation, "The location of the SQL parameter file must be specified in the SqlLoadSimulator configuration file!");

    try {
      final File sqlParameterFile = new File(sqlParameterFileLocation);
      final BufferedReader fileReader = new BufferedReader(new FileReader(sqlParameterFile));
      String sqlArguments = null;

      while ((sqlArguments = fileReader.readLine()) != null) {
        sqlParameters.add(new SqlParameterSource(sqlArguments.split(sqlParameterFileArgumentDelimiter)));
      }

      IoUtil.close(fileReader);

      return sqlParameters;
    }
    catch (FileNotFoundException e) {
      throw new SystemException("The sql parameter file @ location (" + sqlParameterFileLocation
        + ") could not be found!", e);
    }
    catch (IOException e) {
      throw new SystemException("Failed to read sql parameters from file (" + sqlParameterFileLocation + ")!", e);
    }
  }

  public void run() {
    final int sessionAddCount = getConfig().getIntegerPropertyValue("sqlloadsimulator.session.add.count",
      DEFAULT_SESSION_ADD_COUNT);
    final int sessionAddInterval = getConfig().getIntegerPropertyValue("sqlloadsimulator.session.add.interval",
      DEFAULT_SESSION_ADD_INTERVAL);
    final int sessionBeginCount = getConfig().getIntegerPropertyValue("sqlloadsimulator.session.begin.count",
      DEFAULT_SESSION_BEGIN_COUNT);
    final int sessionEndCount = getConfig().getIntegerPropertyValue("sqlloadsimulator.session.end.count",
      DEFAULT_SESSION_END_COUNT);

    final String sessionAddIntervalUnit = getConfig().getStringPropertyValue("sqlloadsimulator.session.add.interval.unit",
      DEFAULT_SESSION_ADD_INTERVAL_UNIT);

    debug("Running load performance test beginning at {0} session(s) and incrementing by {1} every {2} {3}(s) up to {4}...",
      sessionBeginCount, sessionAddCount, sessionAddInterval, sessionAddIntervalUnit, sessionEndCount);

    for (int sessionCount = sessionBeginCount; sessionCount < sessionEndCount; sessionCount += sessionAddCount) {
      scaleUpToWorkers(sessionCount);
      LockSupport.parkUntil(System.currentTimeMillis() + (sessionAddInterval * 1000));
    }
  }

  protected void scaleUpToWorkers(final int count) {
    final String sql = getConfig().getStringPropertyValue("sqlloadsimulator.sql.statement",
      DEFAULT_FAIL_FOR_MISSING_PARAMETERS);

    try {
      while (getWorkers().size() < count) {
        final SqlRunner worker = new SqlRunner(getDataSource().getConnection(), sql);
        getWorkers().add(worker);
        getExecutorService().execute(worker);
      }
    }
    catch (SQLException e) {
      throw new SystemException("Failed to open JDBC connection to data source (" + getDataSource() + ")!", e);
    }
  }

  protected static void debug(final String message, final Object... messageArguments) {
    if (Boolean.getBoolean("app.debug")) {
      System.err.println(MessageFormat.format(message, messageArguments));
    }
  }

  protected static void displayMetrics(final Map<Long, Long> metrics) {
    final StringBuilder buffer = new StringBuilder();
    buffer.append("Sessions          Average Execution Time\n");
    buffer.append("----------------------------------------\n");

    for (final Long sessionCount : metrics.keySet()) {
      buffer.append(StringUtil.pad(String.valueOf(sessionCount), 18));
      buffer.append(metrics.get(sessionCount));
      buffer.append(" ms");
      buffer.append("\n");
    }

    System.out.println(buffer.toString());
  }

  /**
   * Bootstrap method for the SqlLoadSimulator command-line application.
   * @param args command-line arguments to the SqlLoadSimulator application.
   * @throws Exception if the SqlLoadSimulator terminates unexpectedly with some type of error.
   */
  public static void main(final String... args) throws Exception {
    final String userConfigurationFileLocation = System.getProperty("configuration.file");
    final SqlLoadSimulator loadSimulator = new SqlLoadSimulator(userConfigurationFileLocation);

    loadSimulator.run();
    loadSimulator.destroy();
    displayMetrics(loadSimulator.computeMetrics());
  }

  protected class DestroyVisitor implements Visitor {

    public void visit(final Visitable obj) {
      if (obj instanceof Destroyable) {
        ((Destroyable) obj).destroy();
      }
    }
  }

  protected class DriverManagerDataSource extends AbstractDataSource {

    private final String URL;
    private final String username;
    private final String password;

    private DriverManagerDataSource(final String URL, final String username, final String password) {
      Assert.notBlank(URL, "The JDBC URL to the data source must be specified!");
      this.URL = URL;
      this.username = username;
      this.password = password;
    }

    public Connection getConnection() throws SQLException {
      return getConnection(username, password);
    }

    public Connection getConnection(final String username, final String password) throws SQLException {
      return DriverManager.getConnection(getURL(), username, password);
    }

    public String getURL() {
      return URL;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof DriverManagerDataSource)) {
        return false;
      }

      final DriverManagerDataSource that = (DriverManagerDataSource) obj;

      return ObjectUtil.equals(getURL(), that.getURL());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getURL());
      return hashValue;
    }

    @Override
    public String toString() {
      return getURL();
    }
  }

  protected class Metric implements Visitable {

    private final long executionTime;
    private final long numberOfSessions;

    public Metric(final long numberOfSessions, final long executionTime) {
      this.numberOfSessions = numberOfSessions;
      this.executionTime = executionTime;
    }

    public long getExecutionTime() {
      return executionTime;
    }

    public long getNumberOfSessions() {
      return numberOfSessions;
    }

    public void accept(final Visitor visitor) {
      visitor.visit(this);
    }

    @Override
    public String toString() {
      return "Executed SQL statement in (" + getExecutionTime() + ") ms having (" + getNumberOfSessions()
        + ") concurrent sessions!";
    }
  }

  protected class SqlParameterSource implements Iterable<Object> {

    private final List<Integer> parameterTypes = new LinkedList<Integer>();
    private List<Object> parameterValues = new LinkedList<Object>();

    public SqlParameterSource(final Object... values) {
      Assert.notNull(values, "The SQL parameter values cannot be null!");
      parameterValues.addAll(Arrays.asList(values));
      parameterValues = Collections.unmodifiableList(parameterValues);
    }

    public Object get(final int index) {
      return parameterValues.get(index);
    }

    public Iterator<Object> iterator() {
      return parameterValues.iterator();
    }

    public int size() {
      return parameterValues.size();
    }
  }

  protected class SqlRunner implements Destroyable, Interruptable, Runnable, Visitable {

    protected static final int DEFAULT_STOP_ATTEMPTS = 3;

    private boolean destroyed = false;
    private boolean interrupted = false;
    private boolean running = false;

    private final Connection connection;

    private PreparedStatement statement;

    private final String sql;

    public SqlRunner(final Connection connection, final String sql) throws SQLException {
      Assert.notNull(connection, "The connection to the data source cannot be null!");
      Assert.state(is(connection.isClosed()).False(), "The connection to the data source must be open!");
      Assert.notBlank(sql, "The SQL statement must be specified!");
      this.connection = connection;
      this.connection.setAutoCommit(false);
      this.connection.setReadOnly(sql.trim().startsWith("SELECT"));
      this.sql = sql;
    }

    protected Connection getConnection() {
      return connection;
    }

    public boolean isDestroyed() {
      return destroyed;
    }

    public boolean isInterrupted() {
      return interrupted;
    }

    public boolean isRunning() {
      return running;
    }

    protected String getSql() {
      return sql;
    }

    protected boolean continueRunning() throws SQLException {
      return (!isInterrupted() && getConnection().isValid(DEFAULT_CONNECTION_VALIDATION_TIMEOUT_IN_SECONDS));
    }

    public void destroy() {
      int stopAttempts = DEFAULT_STOP_ATTEMPTS;

      interrupt();

      while (isRunning() && stopAttempts > 0) {
        LockSupport.parkUntil(this, System.currentTimeMillis() + 500);
        stopAttempts--;
      }

      // forcefully shutdown regardless if the worker thread is still running
      JdbcUtil.cancelStatement(statement);
      JdbcUtil.closeStatement(statement);
      JdbcUtil.closeConnection(getConnection());

      running = false;
      destroyed = true;
    }

    public void interrupt() {
      interrupted = true;
    }

    public void run() {
      Assert.state(!isDestroyed(), "The worker thread has previously been destroyed cannot be re-run!");

      try {
        running = true;
        statement = getConnection().prepareStatement(getSql());

        while (continueRunning()) {
          final SqlParameterSource sqlParameterSource = getSqlParameterSource();
          int parameterIndex = 1;

          for (final Object parameterValue : sqlParameterSource) {
            statement.setObject(parameterIndex++, parameterValue);
          }

          final long startTime = System.currentTimeMillis();
          final boolean isResultSet = statement.execute();
          final long endTime = System.currentTimeMillis();

          addMetric(new Metric(getWorkers().size(), endTime - startTime));

          if (!isResultSet) {
            debug("The SQL statement updated {0} row(s).", statement.getUpdateCount());
          }
        }
      }
      catch (SQLException e) {
        try {
          JdbcUtil.rollbackTransaction(getConnection());
        }
        catch (Exception ex) {
          throw new SystemException(ex.getMessage(), e);
        }
        throw new SystemException(e);
      }
      finally {
        running = false;
        countDownLock.countDown();
        notifyAll();
      }
    }

    public void accept(final Visitor visitor) {
      visitor.visit(this);
    }
  }

  protected class SumAverageVisitor implements Visitor {

    private long count = 0;
    private long sum = 0;

    protected long getAverage() {
      return (sum / count);
    }

    public void visit(final Visitable obj) {
      if (obj instanceof Metric) {
        sum += ((Metric) obj).getExecutionTime();
        count++;
      }
    }
  }

}
