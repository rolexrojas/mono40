package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.PhoneNumberRecipient;

/**
 * TODO
 *
 * @author hecvasro
 */
class PhoneNumberRecipientItemHolderBinder extends RecipientHolderBinder<PhoneNumberRecipient> {
  @NonNull
  @Override
  String getExtra(@NonNull PhoneNumberRecipient recipient) {
    return recipient.getPhoneNumber().toString();
  }
}
