package com.gbh.movil.domain.recipient;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.Recipient;

/**
 * Invoice recipient representation.
 *
 * @author hecvasro
 */
public class InvoiceRecipient extends Recipient {
  /**
   * Invoice's {@link Biller}.
   */
  private final Biller biller;
  /**
   * Invoice's contract identifier.
   */
  private final String contract;
  /**
   * Invoice's due dueDate.
   */
  private final long dueDate;
  /**
   * Invoice's currency code.
   */
  private final String currency;
  /**
   * Total amount that must be paid.
   */
  private final double totalAmount;
  /**
   * Minimum amount that can be paid.
   */
  private final double minimumAmount;

  /**
   * Constructs a new invoice recipient.
   *
   * @param biller
   *   Invoice's {@link Biller}.
   * @param contract
   *   Invoice's contract identifier.
   * @param dueDate
   *   Invoice's due dueDate.
   * @param currency
   *   Invoice's currency code.
   * @param totalAmount
   *   Total amount that must be paid.
   * @param minimumAmount
   *   Minimum amount that can be paid.
   */
  public InvoiceRecipient(@NonNull Biller biller, @NonNull String contract, long dueDate,
    @NonNull String currency, double totalAmount, double minimumAmount) {
    super(RecipientType.INVOICE);
    this.biller = biller;
    this.contract = contract;
    this.dueDate = dueDate;
    this.currency = currency;
    this.totalAmount = totalAmount;
    this.minimumAmount = minimumAmount;
  }

  /**
   * Gets the {@link Biller} of the invoice.
   *
   * @return Invoice's {@link Biller}.
   */
  @NonNull
  public final Biller getBiller() {
    return biller;
  }

  /**
   * Gets the contract identifier of the invoice.
   *
   * @return Invoice's contract identifier.
   */
  @NonNull
  public final String getContract() {
    return contract;
  }

  /**
   * Gets the due date of the invoice.
   *
   * @return Invoice's due date.
   */
  public final long getDueDate() {
    return dueDate;
  }

  /**
   * Gets the currency code of the invoice.
   *
   * @return Invoice's currency code.
   */
  @NonNull
  public final String getCurrency() {
    return currency;
  }

  /**
   * Gets the total amount that must be paid.
   *
   * @return Total amount that must be paid.
   */
  public final double getTotalAmount() {
    return totalAmount;
  }

  /**
   * Gets the minimum amount that can be paid.
   *
   * @return Minimum amount that can be paid.
   */
  public final double getMinimumAmount() {
    return minimumAmount;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof InvoiceRecipient
      && ((InvoiceRecipient) object).biller.equals(biller)
      && ((InvoiceRecipient) object).contract.equals(contract));
  }

  @Override
  public int hashCode() {
    return Utils.hashCode(type, biller, contract);
  }

  @Override
  public String toString() {
    return InvoiceRecipient.class.getSimpleName() + ":{super=" + super.toString() + ",biller="
      + biller + ",contract='" + contract + "',dueDate=" + dueDate + ",currency='" + currency
      + "',totalAmount=" + totalAmount + ",minimumAmount=" + minimumAmount + "}";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean matches(@NonNull String query) {
    return true;
  }
}
