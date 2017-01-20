package com.gbh.movil.domain.api;

import com.gbh.movil.misc.Result;

/**
 * @author hecvasro
 */
public final class ApiResult<D> extends Result<ApiCode, D> {
  private final ApiError error;

  public ApiResult(ApiCode code, D data, ApiError error) {
    super(code.equals(ApiCode.OK), code, data);
    this.error = error;
  }

  public final ApiError getError() {
    return error;
  }
}
