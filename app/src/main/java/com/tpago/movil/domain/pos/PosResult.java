package com.tpago.movil.domain.pos;

import com.tpago.movil.misc.Result;

/**
 * @author hecvasro
 */
public final class PosResult<D> extends Result<PosCode, D> {
  public PosResult(PosCode code, D data) {
    super(code.equals(PosCode.OK), code, data);
  }
}
