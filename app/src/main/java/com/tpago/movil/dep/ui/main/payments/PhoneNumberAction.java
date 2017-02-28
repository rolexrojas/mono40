package com.tpago.movil.dep.ui.main.payments;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.misc.Utils;

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
