package com.tpago.movil.ui.onboarding.registration;

import com.tpago.movil.PhoneNumber;

/**
 * @author hecvasro
 */
final class RegistrationData {
  private PhoneNumber phoneNumber;
  private PhoneNumber.State phoneNumberState;

  final boolean isValid() {
    return false;
  }

  final PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  final PhoneNumber.State getPhoneNumberState() {
    return phoneNumberState;
  }

  final void setPhoneNumber(PhoneNumber phoneNumber, PhoneNumber.State phoneNumberState) {
    this.phoneNumber = phoneNumber;
    this.phoneNumberState = phoneNumberState;
  }
}
