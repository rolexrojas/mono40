package com.tpago.movil.init.register;

import com.tpago.movil.PhoneNumber;

/**
 * @author hecvasro
 */
final class RegisterData {
  private PhoneNumber phoneNumber;
  private PhoneNumber.State phoneNumberState;
  private String firstName;
  private String lastName;

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

  final String getFirstName() {
    return firstName;
  }

  final String getLastName() {
    return lastName;
  }

  final void setName(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
