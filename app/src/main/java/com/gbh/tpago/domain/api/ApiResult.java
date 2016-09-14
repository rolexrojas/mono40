package com.gbh.tpago.domain.api;

import android.support.annotation.Nullable;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class ApiResult<T> {
  /**
   * TODO
   */
  @ApiCode
  private final int code;

  /**
   * TODO
   */
  private final T data;

  /**
   * TODO
   *
   * @param code TODO
   * @param data TODO
   */
  public ApiResult(@ApiCode int code, @Nullable T data) {
    this.code = code;
    this.data = data;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @ApiCode
  public final int getCode() {
    return code;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  public final T getData() {
    return data;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public final boolean isSuccessful() {
    // TODO
    return false;
  }
}
