package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.PhoneNumber;

/**
 * @author hecvasro
 */
class AddPhoneNumberActionItem extends PhoneNumberActionItem {
  AddPhoneNumberActionItem(@NonNull PhoneNumber phoneNumber) {
    super(ActionType.ADD_PHONE_NUMBER, phoneNumber);
  }

  @Override
  public String toString() {
    return AddPhoneNumberActionItem.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
