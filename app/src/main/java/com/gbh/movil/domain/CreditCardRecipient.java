package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;

/**
 * {@link CreditCard} recipient representation.
 *
 * @author hecvasro
 */
public class CreditCardRecipient extends Recipient {
  private final CreditCard creditCard;

  /**
   * Constructs a new {@link CreditCard} recipient.
   */
  public CreditCardRecipient(@NonNull CreditCard creditCard) {
    super(RecipientType.CREDIT_CARD);
    this.creditCard = creditCard;
  }

  @NonNull
  public final CreditCard getCreditCard() {
    return creditCard;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof CreditCardRecipient
      && ((CreditCardRecipient) object).creditCard.equals(creditCard));
  }

  @Override
  public int hashCode() {
    return Utils.hashCode(type, creditCard);
  }

  @Override
  public String toString() {
    return CreditCardRecipient.class.getSimpleName() + ":{super=" + super.toString()
      + ",creditCard=" + creditCard + "}";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean matches(@NonNull String query) {
    return true;
  }
}
