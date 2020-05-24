package com.mono40.movil.d.data.api;

/**
 * @author hecvasro
 */
@Deprecated
final class ValidatePinRequestBody {
  public static ValidatePinRequestBody create(String pin) {
    return new ValidatePinRequestBody(pin);
  }

  String pin;

  private ValidatePinRequestBody(String pin) {
    this.pin = pin;
  }
}
