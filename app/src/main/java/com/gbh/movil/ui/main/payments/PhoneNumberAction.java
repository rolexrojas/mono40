package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.PhoneNumber;

/**
 * @author hecvasro
 */
abstract class PhoneNumberAction extends Action {
  private final PhoneNumber phoneNumber;

  PhoneNumberAction(@ActionType int type, @NonNull PhoneNumber phoneNumber) {
    super(type);
    this.phoneNumber = phoneNumber;
  }

  @NonNull
  final PhoneNumber getPhoneNumber() {
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
