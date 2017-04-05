package com.tpago.movil.domain;

import static com.tpago.movil.util.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
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
      this.successData = assertNotNull(successData, "successData == null");
      this.failureData = null;
    } else {
      this.successData = null;
      this.failureData = assertNotNull(failureData, "failureData == null");
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
    return "Result{"
      + "successful="
      + successful
      + ", successData='"
      + successData.toString()
      + "', failureData='"
      + failureData.toString()
      + "'}";
  }
}
