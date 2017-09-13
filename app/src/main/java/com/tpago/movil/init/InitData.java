package com.tpago.movil.init;

import com.tpago.movil.PhoneNumber;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class InitData {
  private PhoneNumber phoneNumber;
  @PhoneNumber.State private int phoneNumberState = PhoneNumber.State.NONE;

  InitData() {
  }

  final void setPhoneNumber(PhoneNumber phoneNumber, @PhoneNumber.State int phoneNumberState) {
    this.phoneNumber = Preconditions.assertNotNull(phoneNumber, "phoneNumber == null");
    this.phoneNumberState = phoneNumberState;
  }

  public final PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  @PhoneNumber.State
  public final int getPhoneNumberState() {
    return phoneNumberState;
  }
}
