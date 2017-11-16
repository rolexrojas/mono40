package com.tpago.movil.d.ui.main.recipient.index.category;

import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.UserRecipient;
import rx.functions.Func2;

/**
 * @author Hector Vasquez
 */
final class RecipientComparator implements Func2<Recipient, Recipient, Integer> {

  static RecipientComparator create() {
    return new RecipientComparator();
  }

  private RecipientComparator() {
  }

  @Override
  public Integer call(Recipient rA, Recipient rB) {
    if (rA instanceof UserRecipient) {
      return -1;
    } else if (rB instanceof UserRecipient) {
      return 1;
    } else {
      return rA.getIdentifier()
        .compareTo(rB.getIdentifier());
    }
  }
}
