
package com.cp.tools.lang;

import com.cp.common.lang.ObjectUtil;
import java.net.URL;

public class FindClasspathResource {

  public static void main(final String... args) {
    if (args.length < 1) {
      System.err.println(
        "> java com.codeprimate.tools.bin.FindClasspathResource <resource 0> <resource 1> ... <resource N>");
      System.exit(-1);
    }

    for (final String resource : args) {
      final URL resourceUrl = ClassLoader.getSystemResource(resource);

      if (ObjectUtil.isNotNull(resourceUrl)) {
        System.out.println("found resource (" + resource + ") in (" + resourceUrl + ")");
      }
      else {
        System.out.println(" resource (" + resource + ") not found!");
      }
    }
  }

}
