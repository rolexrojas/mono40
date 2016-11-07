package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

/**
 * @author hecvasro
 */
class TransactionWithPhoneNumberAction extends PhoneNumberAction {
  TransactionWithPhoneNumberAction(@NonNull String phoneNumber) {
    super(ActionType.TRANSACTION_WITH_PHONE_NUMBER, phoneNumber);
  }
}
