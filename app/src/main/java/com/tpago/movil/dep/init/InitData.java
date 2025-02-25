package com.tpago.movil.dep.init;

import com.tpago.movil.PhoneNumber;
import com.tpago.movil.util.ObjectHelper;

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
