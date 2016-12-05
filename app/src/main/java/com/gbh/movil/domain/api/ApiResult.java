package com.gbh.movil.domain.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.util.Result;

/**
 * {@link ApiBridge API}'s result representation.
 *
 * @author hecvasro
 */
public final class ApiResult<D> extends Result<ApiCode, D> {
  /**
   * Constructs a new result.
   *
   * @param code
   *   Result's code.
   * @param data
   *   Result's data.
   */
  private ApiResult(@NonNull ApiCode code, @Nullable D data) {
    super(code, data);
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
   * TODO
   */
  @NonNull
  public static <D> ApiResult<D> create(@NonNull D data) {
    return create(ApiCode.OK, data);
  }

  /**
   * TODO
   */
  @NonNull
  public static <D> ApiResult<D> create() {
    return create(ApiCode.OK);
  }

  @Override
  public boolean isSuccessful() {
    return getCode() == ApiCode.OK;
  }
}
