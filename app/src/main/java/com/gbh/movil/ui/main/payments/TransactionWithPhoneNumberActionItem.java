package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.PhoneNumber;

/**
 * @author hecvasro
 */
class TransactionWithPhoneNumberActionItem extends PhoneNumberActionItem {
  TransactionWithPhoneNumberActionItem(@NonNull PhoneNumber phoneNumber) {
    super(ActionType.TRANSACTION_WITH_PHONE_NUMBER, phoneNumber);
  }

  @Override
  public String toString() {
    return TransactionWithPhoneNumberActionItem.class.getSimpleName() + ":{super="
      + super.toString() + "}";
  }
}
