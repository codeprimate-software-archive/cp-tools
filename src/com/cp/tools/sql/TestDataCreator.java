/*
 * Copyright (c) 2010. Codeprimate, LLC and original author(s).  All Rights Reserved.
 *
 * This software is licensed under the Codeprimate License.  Unless required by applicable law or agreed to in writing, the software is
 * distributed and provided "as is" without any expressed or implied WARRANTIES or CONDITIONS of any kind.  By using the software,
 * the end-user agrees to all terms and conditions of the license.
 */

package com.cp.tools.sql;

import com.cp.common.lang.Assert;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TestDataCreator {

  protected static final int DEFAULT_RECORD_COUNT = 10000;
  protected  static final String DEFAULT_FILE_NAME = "test.dat";

  private static final Random numberGenerator = new Random(System.currentTimeMillis());

  protected static void createData(final File dataFile, int recordCount) throws IOException {
    Assert.notNull(dataFile, "The file to save the data to cannot be null!");

    recordCount = (recordCount > 0 ? recordCount : DEFAULT_RECORD_COUNT);

    if (!dataFile.isFile()) {
      dataFile.createNewFile();
    }

    System.out.println("Writing test data to file (" + dataFile.getAbsolutePath() + ")...");

    final BufferedWriter fileWriter = new BufferedWriter(new FileWriter(dataFile));
    final StringBuilder buffer = new StringBuilder();
    boolean flag = false;

    while (recordCount-- > 0) {
      buffer.delete(0, buffer.length());
      buffer.append(flag ? "\n" : "");
      buffer.append(numberGenerator.nextInt(1000));
      buffer.append(",");
      buffer.append(numberGenerator.nextInt(100));
      buffer.append(",");
      buffer.append(numberGenerator.nextInt(1000));
      fileWriter.write(buffer.toString());
      flag = true;

      if (recordCount % 100 == 0) {
        fileWriter.flush();
      }
    }

    fileWriter.flush();
    fileWriter.close();
  }

  public static void main(final String... args) throws Exception {
    File dataFile = new File(System.getProperty("java.io.tmpdir"), DEFAULT_FILE_NAME);
    int recordCount = DEFAULT_RECORD_COUNT;

    if (args.length > 1) {
      dataFile = new File(args[0]);
      recordCount = Integer.parseInt(args[1]);
    }

    createData(dataFile, recordCount);
  }

}
