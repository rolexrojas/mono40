package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.Recipient;

/**
 * TODO
 *
 * @author hecvasro
 */
abstract class RecipientItem<T extends Recipient> {
  /**
   * TODO
   */
  private final T recipient;

  private boolean selected = false;

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   */
  RecipientItem(@NonNull T recipient) {
    this.recipient = recipient;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  final T getRecipient() {
    return recipient;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  final boolean isSelected() {
    return selected;
  }

  /**
   * TODO
   */
  final boolean toggleSelection() {
    return (selected = !selected);
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
