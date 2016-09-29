package com.gbh.movil.domain.api;

import android.support.annotation.Nullable;

/**
 * API result representation.
 *
 * @author hecvasro
 */
public final class ApiResult<T> {
  /**
   * Result's {@link ApiCode code}.
   */
  @ApiCode
  private final int code;

  /**
   * Result's data.
   */
  private final T data;

  /**
   * Constructs a new API result.
   *
   * @param code
   *   Result's {@link ApiCode code}.
   * @param data
   *   Result's data.
   */
  public ApiResult(@ApiCode int code, @Nullable T data) {
    this.code = code;
    this.data = data;
  }

  /**
   * Gets the {@link ApiCode code} of the result.
   *
   * @return Result's {@link ApiCode code}.
   */
  @ApiCode
  public final int getCode() {
    return code;
  }

  /**
   * Gets the data of the result.
   *
   * @return Result's data.
   */
  @Nullable
  public final T getData() {
    return data;
  }

  /**
   * Indicates whether the result is successful or not.
   *
   * @return True if it is successful, false otherwise.
   */
  public final boolean isSuccessful() {
    return code == ApiCode.SUCCESS;
  }
}
