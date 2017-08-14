package com.tpago.movil.d.ui.main.recipient.index.category;

import static com.tpago.movil.d.ui.main.recipient.index.category.Action.Type.TRANSACTION_WITH_PHONE_NUMBER;

import com.google.auto.value.AutoValue;
import com.tpago.movil.PhoneNumber;

/**
 * @author hecvasro
 */
@AutoValue
abstract class TransactionWithPhoneNumberAction extends PhoneNumberAction {

  static TransactionWithPhoneNumberAction create(PhoneNumber phoneNumber) {
    return new AutoValue_TransactionWithPhoneNumberAction(
      TRANSACTION_WITH_PHONE_NUMBER,
      phoneNumber
    );
  }
}
