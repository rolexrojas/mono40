package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.PhoneNumber;

/**
 * @author hecvasro
 */
class AddPhoneNumberAction extends PhoneNumberAction {
  AddPhoneNumberAction(@NonNull String phoneNumber) {
    super(ActionType.ADD_PHONE_NUMBER, phoneNumber);
  }

  @Override
  public String toString() {
    return AddPhoneNumberAction.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
