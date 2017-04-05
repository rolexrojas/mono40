package com.tpago.movil.init;

import com.tpago.movil.PhoneNumber;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class InitData {
  private PhoneNumber phoneNumber;
  private PhoneNumber.State phoneNumberState = PhoneNumber.State.NONE;

  InitData() {
  }

  final void setPhoneNumber(PhoneNumber phoneNumber, PhoneNumber.State phoneNumberState) {
    this.phoneNumber = Preconditions.assertNotNull(phoneNumber, "phoneNumber == null");
    this.phoneNumberState = Preconditions.assertNotNull(phoneNumberState, "phoneNumberState == null");
  }

  public final PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  public final PhoneNumber.State getPhoneNumberState() {
    return phoneNumberState;
  }
}
