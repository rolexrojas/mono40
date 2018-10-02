package com.tpago.movil.d.domain.pos;

import com.cube.sdk.storage.operation.CubeError;
import com.cube.sdk.storage.operation.PaymentInfo;
import com.tpago.movil.d.misc.Result;
import com.tpago.movil.util.digit.DigitUtil;

@Deprecated
public final class PosResult extends Result<PosCode, String> {

  public static PosResult create(String data) {
    return new PosResult(PosCode.OK, data);
  }

  public static PosResult create(PaymentInfo data) {
    final String s = new StringBuilder()
      .append("{")
      .append("value:")
      .append("'")
      .append(data.getValue())
      .append("'")
      .append(",")
      .append("date:")
      .append("'")
      .append(data.getDate())
      .append("'")
      .append(",")
      .append("reference:")
      .append("'")
      .append(data.getReference())
      .append("'")
      .append(",")
      .append("time:")
      .append("'")
      .append(data.getTime())
      .append("'")
      .append(",")
      .append("crypto:")
      .append("'")
      .append(data.getCrypto())
      .append("'")
      .append(",")
      .append("atc:")
      .append("'")
      .append(data.getAtc())
      .append("'")
      .append("}")
      .toString();
    return new PosResult(PosCode.OK, s);
  }

  public static PosResult create(CubeError error) {
    final int code = Integer.parseInt(error.getErrorCode());
    final String data = new StringBuilder()
      .append("{")
      .append("code:")
      .append(code)
      .append(",")
      .append("message:")
      .append("'")
      .append(error.getErrorMessage())
      .append("'")
      .append(",")
      .append("details:")
      .append("'")
      .append(error.getErrorDetails())
      .append("'")
      .append("}")
      .toString();
    return new PosResult(PosCode.fromValue(code), data);
  }

  private PosResult(PosCode code, String data) {
    super(code.equals(PosCode.OK), code, data);
  }
}
