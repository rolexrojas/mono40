package com.gbh.movil.domain.session;

import com.gbh.movil.misc.Result;

/**
 * @author hecvasro
 */
public final class AuthResult extends Result<AuthCode, Session> {
  /**
   * Constructs a new result.
   */
  public AuthResult(AuthCode code, Session data) {
    super(code.equals(AuthCode.SUCCEEDED), code, data);
  }
}
