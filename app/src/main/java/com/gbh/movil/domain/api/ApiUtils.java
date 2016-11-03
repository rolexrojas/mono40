package com.gbh.movil.domain.api;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Result;

/**
 * Api utilities methods.
 *
 * @author hecvasro
 */
public final class ApiUtils {
  private ApiUtils() {
  }

  /**
   * Indicates whether the given {@link Result result} is successful or not.
   *
   * @param result
   *   {@link Result} that will be evaluated.
   *
   * @return True if it is successful, false otherwise.
   */
  public static <D> boolean isSuccessful(@NonNull Result<ApiCode, D> result) {
    return result.getCode() == ApiCode.OK;
  }
}
