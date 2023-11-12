
package com.cp.tools.lang;

public class LoadClass {

  public static void main(final String... args) {
    if (args.length < 1) {
      System.err.println("> java com.codeprimate.tools.bin.LoadClass <fully-qualified class name 0> ... <fully-qualified class name N>");
      System.exit(-1);
    }

    for (final String className : args) {
      try {
        Class.forName(className);
        System.out.println("class (" + className + ") exists in the CLASSPATH!");
      }
      catch (ClassNotFoundException e) {
        System.err.println("class (" + className + ") was not found in the CLASSPATH!");
      }
    }
  }

}
