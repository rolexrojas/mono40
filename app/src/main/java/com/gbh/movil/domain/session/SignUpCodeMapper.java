package com.gbh.movil.domain.session;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.api.ApiCode;

/**
 * @author hecvasro
 */
final class SignUpCodeMapper extends AuthCodeMapper {
  @NonNull
  @Override
  public AuthCode map(@NonNull ApiCode code) {
    switch (code) {
      case OK:
        return AuthCode.SUCCEEDED;
      case BAD_REQUEST:
        return AuthCode.FAILED_ALREADY_ASSOCIATED_PROFILE;
      case UNAUTHORIZED:
        return AuthCode.FAILED_INCORRECT_PIN;
      default:
        return AuthCode.FAILED_UNEXPECTED;
    }
  }
}
