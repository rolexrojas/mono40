package com.tpago.movil.init.register;

import com.tpago.movil.PhoneNumber;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
final class RegisterData {
  private PhoneNumber phoneNumber;
  private PhoneNumber.State phoneNumberState = PhoneNumber.State.NONE;
  private String firstName;
  private String lastName;

  final PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  final PhoneNumber.State getPhoneNumberState() {
    return phoneNumberState;
  }

  final void setPhoneNumber(PhoneNumber phoneNumber, PhoneNumber.State phoneNumberState) {
    this.phoneNumber = Preconditions.checkNotNull(phoneNumber, "phoneNumber == null");
    this.phoneNumberState = Preconditions.checkNotNull(phoneNumberState, "phoneNumberState == null");
  }

  final String getFirstName() {
    return firstName;
  }

  final String getLastName() {
    return lastName;
  }

  final void setName(String firstName, String lastName) {
    this.firstName = Preconditions.checkNotNull(firstName, "firstName == null");
    this.lastName = Preconditions.checkNotNull(lastName, "lastName == null");
  }
}
