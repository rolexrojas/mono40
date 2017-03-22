package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.d.misc.Utils;

import java.math.BigDecimal;

/**
 * Transaction representation.
 */
public class Transaction {
  /**
   * Transaction's type.
   */
  private final String type;
  /**
   * Recipient's name.
   */
  private final String name;
  /**
   * Transaction's date.
   */
  private final long date;
  /**
   * Transaction's {@link RequestType request type}.
   */
  private final RequestType requestType;
  /**
   * Transaction's currency code.
   */
  private final String currency;
  /**
   * Transaction's getValueContent.
   */
  private final BigDecimal value;

  /**
   * Constructs a new transaction.
   *
   * @param type
   *   Transaction's type.
   * @param name
   *   Recipient's name.
   * @param date
   *   Transaction's date.
   * @param requestType
   *   Transaction's {@link RequestType request type}.
   * @param currency
   *   Transaction's currency code.
   * @param value
   *   Transaction's getValueContent.
   */
  public Transaction(@NonNull String type, @NonNull String name, long date,
    RequestType requestType, @NonNull String currency, BigDecimal value) {
    this.type = type;
    this.name = name;
    this.date = date;
    this.requestType = requestType;
    this.currency = currency;
    this.value = value;
  }

  /**
   * Gets the type of the transaction.
   *
   * @return Transaction's type.
   */
  @NonNull
  public final String getType() {
    return type;
  }

  /**
   * Gets the name of the recipient.
   *
   * @return Recipient's name.
   */
  @NonNull
  public final String getName() {
    return name;
  }

  /**
   * Gets the date of the transaction.
   *
   * @return Transaction's date.
   */
  public final long getDate() {
    return date;
  }

  /**
   * Gets the {@link RequestType request type} of the transaction.
   *
   * @return Transaction's {@link RequestType request type}.
   */
  @NonNull
  public final RequestType getRequestType() {
    return requestType;
  }

  /**
   * Gets the currency code of the transaction.
   *
   * @return Transaction's currency code.
   */
  @NonNull
  public final String getCurrency() {
    return currency;
  }

  /**
   * Gets the getValueContent of the transaction.
   *
   * @return Transaction's getValueContent.
   */
  @NonNull
  public final BigDecimal getValue() {
    return value;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof Transaction
      && ((Transaction) object).type.equals(type) && ((Transaction) object).name.equals(name));
  }

  @Override
  public int hashCode() {
    return Utils.hashCode(type, name);
  }

  @Override
  public String toString() {
    return Transaction.class.getSimpleName() + ":{type='" + type + "',name='" + name + "',date='"
      + date + "'requestType=" + requestType + ",currency='" + currency + "',getValueContent=" + value
      + "}";
  }

  /**
   * Transaction request type enumeration.
   */
  public enum RequestType {
    D, // Debit
    C // Credit
  }
}
