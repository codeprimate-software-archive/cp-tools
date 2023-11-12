
package com.cp.tools.sqlloader.io.parser;

import com.cp.tools.sqlloader.io.util.FileType;
import com.cp.common.io.FileUtil;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class InputParserFactory implements ApplicationContextAware {

  private static final Map<FileType, String> INPUT_PARSER_MAP = new TreeMap<FileType, String>();

  static {
    INPUT_PARSER_MAP.put(FileType.CSV, "csvParser");
    INPUT_PARSER_MAP.put(FileType.FIXED_WIDTH, "fixedWidthParser");
    INPUT_PARSER_MAP.put(FileType.XLS, "xlsParser");
  }

  private ApplicationContext context;

  protected ApplicationContext getApplicationContext() {
    Assert.state(ObjectUtil.isNotNull(context), "The Spring Application Context was not properly initialized!");
    return context;
  }

  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    Assert.notNull(applicationContext, "The Spring Application Context cannot be null!");
    this.context = applicationContext;
  }

  public InputParser getInputParser(final File inputFile) {
    final String fileExtension = FileUtil.getFileExtension(inputFile);
    final FileType fileType = FileType.getByFileExtension(fileExtension);

    Assert.notNull(fileType, "The file type (" + fileExtension + ") is not a recognized file type to parse by the SqlLoader application!");

    return (InputParser) getApplicationContext().getBean(INPUT_PARSER_MAP.get(fileType));
  }

}
