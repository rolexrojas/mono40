package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.Item;

/**
 * @author hecvasro
 */

class RecipientItem implements Item {
  private final Recipient recipient;

  RecipientItem(@NonNull Recipient recipient) {
    this.recipient = recipient;
  }

  final Recipient getRecipient() {
    return recipient;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof RecipientItem
      && ((RecipientItem) object).recipient.equals(recipient));
  }

  @Override
  public int hashCode() {
    return recipient.hashCode();
  }

  @Override
  public String toString() {
    return RecipientItem.class.getSimpleName() + ":{recipient=" + recipient + "}";
  }
}
