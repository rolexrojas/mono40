package com.tpago.movil.d.domain.pos;

import com.tpago.movil.d.misc.Result;

/**
 * @author hecvasro
 */
public final class PosResult extends Result<PosCode, String> {
  public PosResult(PosCode code, String data) {
    super(code.equals(PosCode.OK), code, data);
  }
}
