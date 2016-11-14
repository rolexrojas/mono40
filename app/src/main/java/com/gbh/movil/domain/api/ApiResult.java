package com.gbh.movil.domain.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Result representation.
 *
 * @author hecvasro
 */
public final class ApiResult<D> {
  /**
   * Result's code.
   */
  private final ApiCode code;
  /**
   * Result's data.
   */
  private final D data;

  /**
   * Constructs a new result.
   *
   * @param code
   *   Result's code.
   * @param data
   *   Result's data.
   */
  private ApiResult(@NonNull ApiCode code, @Nullable D data) {
    this.code = code;
    this.data = data;
  }

  /**
   * Creates a {@link ApiResult result} with the given code and data.
   */
  @NonNull
  public static <D> ApiResult<D> create(@NonNull ApiCode code, @Nullable D data) {
    return new ApiResult<>(code, data);
  }

  /**
   * Creates a {@link ApiResult result} with the given code.
   */
  @NonNull
  public static <D> ApiResult<D> create(@NonNull ApiCode code) {
    return create(code, null);
  }

  /**
   * Gets the code of the result.
   *
   * @return Result's code.
   */
  @NonNull
  public final ApiCode getCode() {
    return code;
  }

  /**
   * Gets the data of the result.
   *
   * @return Result's data.
   */
  @Nullable
  public final D getData() {
    return data;
  }

  /**
   * Indicates if is successful result or not.
   *
   * @return True if it is successful result, false otherwise.
   */
  public final boolean isSuccessful() {
    return code == ApiCode.OK;
  }

  @Override
  public String toString() {
    return "Result:{code='" + code + "',data='" + data + "'}";
  }
}
