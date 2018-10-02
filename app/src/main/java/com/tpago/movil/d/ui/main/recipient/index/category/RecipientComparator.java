package com.tpago.movil.d.ui.main.recipient.index.category;

import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.UserRecipient;

import java.util.Comparator;

/**
 * @author Hector Vasquez
 */
final class RecipientComparator implements Comparator<Recipient> {

  static RecipientComparator create() {
    return new RecipientComparator();
  }

  private RecipientComparator() {
  }

  @Override
  public int compare(Recipient a, Recipient b) {
    if (a instanceof UserRecipient) {
      return -1;
    } else if (b instanceof UserRecipient) {
      return 1;
    } else {
      return a.getIdentifier()
        .compareTo(b.getIdentifier());
    }
  }
}
