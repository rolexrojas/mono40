package com.mono40.movil.job;

import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class JobHelper {

  public static String createGroupId(Class<?> jobType) {
    return ObjectHelper.checkNotNull(jobType, "jobType")
      .getCanonicalName();
  }

  public static String createTag(Class<?> type) {
    return ObjectHelper.checkNotNull(type, "type")
      .getCanonicalName();
  }

  private JobHelper() {
  }
}
