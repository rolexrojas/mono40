package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.PhoneNumberRecipient;
import com.gbh.movil.domain.Recipient;

/**
 * TODO
 *
 * @author hecvasro
 */
final class RecipientItemCreator {
  private RecipientItemCreator() {
  }

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  static RecipientItem create(@NonNull Recipient recipient) {
    switch (recipient.getType()) {
      case PHONE_NUMBER:
        return new PhoneNumberRecipientItem((PhoneNumberRecipient) recipient);
      default:
        return null;
    }
  }
}
