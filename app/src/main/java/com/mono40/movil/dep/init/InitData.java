package com.mono40.movil.dep.init;

import com.mono40.movil.PhoneNumber;
import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class InitData {

  private PhoneNumber phoneNumber;
  @PhoneNumber.State private int phoneNumberState = PhoneNumber.State.NONE;

  InitData() {
  }

  final void setPhoneNumber(PhoneNumber phoneNumber, @PhoneNumber.State int phoneNumberState) {
    this.phoneNumber = ObjectHelper.checkNotNull(phoneNumber, "phoneNumber");
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
