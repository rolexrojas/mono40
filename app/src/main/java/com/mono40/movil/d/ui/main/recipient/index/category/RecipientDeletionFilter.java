package com.mono40.movil.d.ui.main.recipient.index.category;

import static com.mono40.movil.d.domain.RecipientType.ACCOUNT;
import static com.mono40.movil.d.domain.RecipientType.BILL;
import static com.mono40.movil.d.domain.RecipientType.NON_AFFILIATED_PHONE_NUMBER;
import static com.mono40.movil.d.domain.RecipientType.PHONE_NUMBER;

import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.domain.RecipientType;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.functions.Predicate;

/**
 * @author Hector Vasquez
 */
final class RecipientDeletionFilter implements Predicate<Recipient> {

  private static final Set<RecipientType> DELETABLE;

  static {
    DELETABLE = new HashSet<>();
    DELETABLE.add(ACCOUNT);
    DELETABLE.add(BILL);
    DELETABLE.add(NON_AFFILIATED_PHONE_NUMBER);
    DELETABLE.add(PHONE_NUMBER);
  }

  static RecipientDeletionFilter create(boolean deleting) {
    return new RecipientDeletionFilter(deleting);
  }

  private final boolean deleting;

  private RecipientDeletionFilter(boolean deleting) {
    this.deleting = deleting;
  }

  @Override
  public boolean test(Recipient recipient) {
    return !this.deleting || DELETABLE.contains(recipient.getType());
  }
}
