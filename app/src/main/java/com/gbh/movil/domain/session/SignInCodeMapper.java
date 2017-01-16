package com.gbh.movil.domain.session;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.api.ApiCode;

/**
 * @author hecvasro
 */
final class SignInCodeMapper extends AuthCodeMapper {
  @NonNull
  @Override
  public AuthCode map(@NonNull ApiCode code) {
    switch (code) {
      case OK:
        return AuthCode.SUCCEEDED;
      case BAD_REQUEST:
        return AuthCode.FAILED_ALREADY_ASSOCIATED_DEVICE;
      case UNAUTHORIZED:
        return AuthCode.FAILED_INCORRECT_USERNAME_AND_PASSWORD;
      default:
        return AuthCode.FAILED_UNEXPECTED;
    }
  }
}
