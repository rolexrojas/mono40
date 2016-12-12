package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.PhoneNumber;

/**
 * @author hecvasro
 */
class TransactionWithPhoneNumberAction extends PhoneNumberAction {
  TransactionWithPhoneNumberAction(@NonNull String phoneNumber) {
    super(ActionType.TRANSACTION_WITH_PHONE_NUMBER, phoneNumber);
  }

  @Override
  public String toString() {
    return TransactionWithPhoneNumberAction.class.getSimpleName() + ":{super="
      + super.toString() + "}";
  }
}
