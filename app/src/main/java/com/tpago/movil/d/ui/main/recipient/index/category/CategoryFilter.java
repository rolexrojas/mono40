package com.tpago.movil.d.ui.main.recipient.index.category;

import static com.tpago.movil.d.domain.Recipient.acceptsPayments;
import static com.tpago.movil.d.domain.Recipient.acceptsRecharges;
import static com.tpago.movil.d.domain.Recipient.acceptsTransfers;
import static com.tpago.movil.util.Preconditions.assertNotNull;

import com.tpago.movil.d.domain.Recipient;
import rx.functions.Func1;

/**
 * @author Hector Vasquez
 */
final class CategoryFilter implements Func1<Recipient, Boolean> {

  static CategoryFilter create(Category category) {
    return new CategoryFilter(category);
  }

  private final Category category;

  private CategoryFilter(Category category) {
    this.category = assertNotNull(category, "category == null");
  }

  @Override
  public Boolean call(Recipient recipient) {
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
