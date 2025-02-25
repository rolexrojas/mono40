package com.tpago.movil.d.domain;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
public final class Result<T, C> {

  public static <T, C> Result<T, C> create(T successData) {
    return new Result<>(true, successData, null);
  }

  public static <T, C> Result<T, C> create(FailureData<C> failureData) {
    return new Result<>(false, null, failureData);
  }

  private final boolean successful;

  private final T successData;
  private final FailureData<C> failureData;

  private Result(boolean successful, T successData, FailureData<C> failureData) {
    this.successful = successful;
    if (successful) {
      this.successData = ObjectHelper.checkNotNull(successData, "successData");
      this.failureData = null;
    } else {
      this.successData = null;
      this.failureData = ObjectHelper.checkNotNull(failureData, "failureData");
    }
  }

  public final boolean isSuccessful() {
    return successful;
  }

  public final T getSuccessData() {
    return successData;
  }

  public final FailureData<C> getFailureData() {
    return failureData;
  }

  @Override
  public String toString() {
    String failureDataString = "";
    String succssDataString = "";

    if (ObjectHelper.isNotNull(failureData)) {
      failureDataString = failureDataString;
    }

    if (ObjectHelper.isNotNull(successData)) {
      succssDataString = failureDataString;
    }

    return "Result{"
      + "successful="
      + successful
      + ", successData='"
      + failureDataString
      + "', failureData='"
      + succssDataString
      + "'}";
  }
}
