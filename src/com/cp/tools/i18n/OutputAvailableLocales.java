
package com.cp.tools.i18n;

import java.util.Arrays;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

public class OutputAvailableLocales {

  public static void main(final String... args) {
    final Set<String> timeZoneIdSet = new TreeSet<String>(Arrays.asList(TimeZone.getAvailableIDs()));

    for (final String timeZoneId : timeZoneIdSet) {
      System.out.println(timeZoneId);
    }
  }

}
