package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

/**
 * @author hecvasro
 */
class AddPhoneNumberAction extends PhoneNumberAction {
  AddPhoneNumberAction(@NonNull String phoneNumber) {
    super(ActionType.ADD_PHONE_NUMBER, phoneNumber);
  }
}
