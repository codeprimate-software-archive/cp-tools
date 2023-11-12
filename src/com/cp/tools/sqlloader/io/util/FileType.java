
package com.cp.tools.sqlloader.io.util;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;

public enum FileType {
  CSV("csv", "Comma Seperated Values"),
  FIXED_WIDTH("fxw", "Fixed Width File"),
  XLS("xls", "Microsoft Excel Spreadsheet");

  private final String description;
  private final String fileExtension;

  FileType(final String fileExtension, final String description) {
    Assert.notEmpty(fileExtension, "The file extension cannot be null or empty!");
    Assert.notEmpty(description, "The file type's description cannot be null or empty!");
    this.fileExtension = fileExtension;
    this.description = description;
  }

  public static FileType getByFileExtension(final String fileExtension) {
    for (final FileType fileType : values()) {
      if (ObjectUtil.equals(fileType.getFileExtension(), fileExtension)) {
        return fileType;
      }
    }

    return null;
  }

  public String getDescription() {
    return description;
  }

  public String getFileExtension() {
    return fileExtension;
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer(getDescription());
    buffer.append("(.").append(getFileExtension()).append(" file)");
    return buffer.toString();
  }

}
