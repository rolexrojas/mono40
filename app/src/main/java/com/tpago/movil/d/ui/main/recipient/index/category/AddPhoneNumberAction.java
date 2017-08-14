package com.tpago.movil.d.ui.main.recipient.index.category;

import android.support.annotation.NonNull;

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
