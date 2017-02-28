package com.tpago.movil.dep.ui.main.payments;

import android.support.annotation.NonNull;

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
