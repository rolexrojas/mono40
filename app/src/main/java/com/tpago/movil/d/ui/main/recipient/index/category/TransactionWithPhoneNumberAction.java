package com.tpago.movil.d.ui.main.recipient.index.category;

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
