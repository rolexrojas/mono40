package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.main.list.Item;

/**
 * TODO
 *
 * @author hecvasro
 */
abstract class RecipientItem<T extends Recipient> implements Item {
  private final T recipient;

  private boolean selected = false;

  RecipientItem(@NonNull T recipient) {
    this.recipient = recipient;
  }

  @NonNull
  final T getRecipient() {
    return recipient;
  }

  final boolean isSelected() {
    return selected;
  }

  void setSelected(boolean selected) {
    this.selected = selected;
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
    return RecipientItem.class.getSimpleName() + ":{recipient=" + recipient + "selected='"
      + selected + "'}";
  }
}
