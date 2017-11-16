package com.tpago.movil.d.domain.api;

import com.tpago.movil.d.misc.Result;

/**
 * @author hecvasro
 */
@Deprecated
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
