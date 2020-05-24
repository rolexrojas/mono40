package com.mono40.movil.d.ui.main.recipient.index.category;

import static com.mono40.movil.d.domain.Recipient.acceptsPayments;
import static com.mono40.movil.d.domain.Recipient.acceptsRecharges;
import static com.mono40.movil.d.domain.Recipient.acceptsTransfers;

import com.mono40.movil.d.domain.Recipient;

import io.reactivex.functions.Predicate;

/**
 * @author Hector Vasquez
 */
final class CategoryFilter implements Predicate<Recipient> {

  static CategoryFilter create(Category category) {
    return new CategoryFilter(category);
  }

  private final Category category;

  private CategoryFilter(Category category) {
    this.category = category;
  }

  @Override
  public boolean test(Recipient recipient) throws Exception {
    switch (this.category) {
      case PAY:
        return acceptsPayments(recipient);
      case TRANSFER:
        return acceptsTransfers(recipient);
      case RECHARGE:
        return acceptsRecharges(recipient);
      default:
        return false;
    }
  }
}
