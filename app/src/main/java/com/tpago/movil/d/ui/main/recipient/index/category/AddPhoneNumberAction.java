package com.tpago.movil.d.ui.main.recipient.index.category;

import static com.tpago.movil.d.ui.main.recipient.index.category.Action.Type.ADD_PHONE_NUMBER;

import com.google.auto.value.AutoValue;
import com.tpago.movil.domain.PhoneNumber;

/**
 * @author hecvasro
 */
@AutoValue
abstract class AddPhoneNumberAction extends PhoneNumberAction {

  static AddPhoneNumberAction create(PhoneNumber phoneNumber) {
    return new AutoValue_AddPhoneNumberAction(ADD_PHONE_NUMBER, phoneNumber);
  }
}
