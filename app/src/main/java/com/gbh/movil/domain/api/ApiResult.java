package com.gbh.movil.domain.api;

import com.gbh.movil.misc.Result;

/**
 * @author hecvasro
 */
public final class ApiResult<D> extends Result<ApiCode, D> {
  /**
   * Constructs a new result.
   */
  public ApiResult(ApiCode code, D data) {
    super(code.equals(ApiCode.OK), code, data);
  }
}
