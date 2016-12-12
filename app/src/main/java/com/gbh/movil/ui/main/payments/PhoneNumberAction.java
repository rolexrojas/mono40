package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.misc.Utils;

/**
 * @author hecvasro
 */
abstract class PhoneNumberAction extends Action {
  private final String phoneNumber;

  PhoneNumberAction(@ActionType int type, @NonNull String phoneNumber) {
    super(type);
    this.phoneNumber = phoneNumber;
  }

  @NonNull
  final String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof PhoneNumberAction
      && ((PhoneNumberAction) object).getType() == getType()
      && ((PhoneNumberAction) object).phoneNumber.equals(phoneNumber));
  }

  @Override
  public int hashCode() {
    return Utils.hashCode(getType(), phoneNumber);
  }

  @Override
  public String toString() {
    return PhoneNumberAction.class.getSimpleName() + ":{phoneNumber=" + phoneNumber + "}";
  }
}
