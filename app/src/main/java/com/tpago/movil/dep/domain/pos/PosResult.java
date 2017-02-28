package com.tpago.movil.dep.domain.pos;

import com.tpago.movil.dep.misc.Result;

/**
 * @author hecvasro
 */
public final class PosResult extends Result<PosCode, String> {
  public PosResult(PosCode code, String data) {
    super(code.equals(PosCode.OK), code, data);
  }
}
