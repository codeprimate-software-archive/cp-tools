
package com.cp.tools.sqlloader.test;

import com.cp.tools.sqlloader.common.converter.TrimmedStringConverterTest;
import com.cp.tools.sqlloader.common.parser.ParserUtilTest;
import com.cp.tools.sqlloader.dao.AbstractLoaderDaoTest;
import com.cp.tools.sqlloader.dao.DefaultLoaderDaoTest;
import com.cp.tools.sqlloader.dao.support.CalendarToTimestampConverterTest;
import com.cp.tools.sqlloader.dao.support.NullToDefaultValueConverterTest;
import com.cp.tools.sqlloader.dao.support.ObjectTypeToJdbcTypeConverterTest;
import com.cp.tools.sqlloader.dao.support.ObjectTypeToSqlTypeConverterTest;
import com.cp.tools.sqlloader.dao.support.StringToUpperVarcharConverterTest;
import com.cp.tools.sqlloader.io.parser.AbstractInputParserTest;
import com.cp.tools.sqlloader.io.parser.DelimitedInputParserTest;
import com.cp.tools.sqlloader.io.parser.InputParserFactoryTest;
import com.cp.tools.sqlloader.io.parser.support.CsvInputParserTest;
import com.cp.tools.sqlloader.io.parser.support.FixedWidthInputParserTest;
import com.cp.tools.sqlloader.io.parser.support.XlsInputParserTest;
import com.cp.tools.sqlloader.mapping.beans.AbstractColumnDefinitionTest;
import com.cp.tools.sqlloader.mapping.beans.AbstractConstraintDefinitionTest;
import com.cp.tools.sqlloader.mapping.beans.AbstractDataSourceDefinitionTest;
import com.cp.tools.sqlloader.mapping.beans.AbstractTableDefinitionTest;
import com.cp.tools.sqlloader.mapping.beans.MappingBeanFactoryTest;
import com.cp.tools.sqlloader.mapping.parser.DefaultMappingParserTest;
import com.cp.tools.sqlloader.mapping.util.ColumnDefinitionConverterTest;
import com.cp.tools.sqlloader.mapping.util.ConstraintDefinitionConverterTest;
import com.cp.tools.sqlloader.mapping.util.TableDefinitionConverterTest;
import com.cp.tools.sqlloader.sql.AbstractDelegatingDataSourceTest;
import com.cp.tools.sqlloader.sql.support.AnsiSqlDialectFactoryTest;
import com.cp.tools.sqlloader.sql.support.DriverManagerDelegatingDataSourceTest;
import com.cp.tools.sqlloader.sql.util.SqlUtilTest;
import com.cp.tools.sqlloader.validation.AbstractBoundedConstraintTest;
import com.cp.tools.sqlloader.validation.AbstractConstraintTest;
import com.cp.tools.sqlloader.validation.ComposableConstraintTest;
import com.cp.tools.sqlloader.validation.support.AlphaNumericConstraintTest;
import com.cp.tools.sqlloader.validation.support.BoundedNumericValueConstraintTest;
import com.cp.tools.sqlloader.validation.support.BoundedStringLengthConstraintTest;
import com.cp.tools.sqlloader.validation.support.BoundedTimeConstraintTest;
import com.cp.tools.sqlloader.validation.support.DigitsOnlyConstraintTest;
import com.cp.tools.sqlloader.validation.support.LettersOnlyConstraintTest;
import com.cp.tools.sqlloader.validation.support.NotEmptyConstraintTest;
import com.cp.tools.sqlloader.validation.support.NotNullConstraintTest;
import com.cp.tools.sqlloader.validation.support.RegexConstraintTest;
import com.cp.tools.sqlloader.validation.support.UniqueConstraintTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllSqlLoaderTests extends TestCase {

  public AllSqlLoaderTests(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTest(getCommonConverterTests());
    suite.addTest(getCommonParserTests());
    suite.addTest(getDaoTests());
    suite.addTest(getDaoSupportTests());
    suite.addTest(getIoParserTests());
    suite.addTest(getIoParserSupportTests());
    suite.addTest(getMappingBeansTests());
    suite.addTest(getMappingParserTests());
    suite.addTest(getMappingUtilTests());
    suite.addTest(getSqlTests());
    suite.addTest(getSqlSupportTests());
    suite.addTest(getSqlUtilTests());
    suite.addTest(getValidationTests());
    suite.addTest(getValidationSupportTests());
    return suite;
  }

  private static Test getCommonConverterTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(TrimmedStringConverterTest.suite());
    return suite;
  }

  private static Test getCommonParserTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(ParserUtilTest.suite());
    return suite;
  }

  private static Test getDaoTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(AbstractLoaderDaoTest.suite());
    suite.addTest(DefaultLoaderDaoTest.suite());
    return suite;
  }

  private static Test getDaoSupportTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(CalendarToTimestampConverterTest.suite());
    suite.addTest(NullToDefaultValueConverterTest.suite());
    suite.addTest(ObjectTypeToJdbcTypeConverterTest.suite());
    suite.addTest(ObjectTypeToSqlTypeConverterTest.suite());
    suite.addTest(StringToUpperVarcharConverterTest.suite());
    return suite;
  }

  public static Test getIoParserTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(AbstractInputParserTest.suite());
    suite.addTest(DelimitedInputParserTest.suite());
    suite.addTest(InputParserFactoryTest.suite());
    return suite;
  }

  private static Test getIoParserSupportTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(CsvInputParserTest.suite());
    suite.addTest(FixedWidthInputParserTest.suite());
    suite.addTest(XlsInputParserTest.suite());
    return suite;
  }

  public static Test getMappingBeansTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(AbstractColumnDefinitionTest.suite());
    suite.addTest(AbstractConstraintDefinitionTest.suite());
    suite.addTest(AbstractDataSourceDefinitionTest.suite());
    suite.addTest(AbstractTableDefinitionTest.suite());
    suite.addTest(MappingBeanFactoryTest.suite());
    return suite;
  }

  private static Test getMappingParserTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(DefaultMappingParserTest.suite());
    return suite;
  }

  private static Test getMappingUtilTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(ColumnDefinitionConverterTest.suite());
    suite.addTest(ConstraintDefinitionConverterTest.suite());
    suite.addTest(TableDefinitionConverterTest.suite());
    return suite;
  }

  private static Test getSqlTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(AbstractDelegatingDataSourceTest.suite());
    return suite;
  }

  private static Test getSqlSupportTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(AnsiSqlDialectFactoryTest.suite());
    suite.addTest(DriverManagerDelegatingDataSourceTest.suite());
    return suite;
  }

  private static Test getSqlUtilTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(SqlUtilTest.suite());
    return suite;
  }

  private static Test getValidationTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(AbstractBoundedConstraintTest.suite());
    suite.addTest(AbstractConstraintTest.suite());
    suite.addTest(ComposableConstraintTest.suite());
    return suite;
  }

  private static Test getValidationSupportTests() {
    final TestSuite suite = new TestSuite();
    suite.addTest(AlphaNumericConstraintTest.suite());
    suite.addTest(BoundedNumericValueConstraintTest.suite());
    suite.addTest(BoundedStringLengthConstraintTest.suite());
    suite.addTest(BoundedTimeConstraintTest.suite());
    suite.addTest(DigitsOnlyConstraintTest.suite());
    suite.addTest(LettersOnlyConstraintTest.suite());
    suite.addTest(NotEmptyConstraintTest.suite());
    suite.addTest(NotNullConstraintTest.suite());
    suite.addTest(RegexConstraintTest.suite());
    suite.addTest(UniqueConstraintTest.suite());
    return suite;
  }

}
