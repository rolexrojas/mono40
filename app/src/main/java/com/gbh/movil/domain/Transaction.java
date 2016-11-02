package com.gbh.movil.domain;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
  @RequestType
  private final int requestType;
  /**
   * Transaction's currency code.
   */
  private final String currency;
  /**
   * Transaction's value.
   */
  private final double value;

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
   *   Transaction's value.
   */
  public Transaction(@NonNull String type, @NonNull String name, long date,
    @RequestType int requestType, @NonNull String currency, double value) {
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
  @RequestType
  public final int getRequestType() {
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
   * Gets the value of the transaction.
   *
   * @return Transaction's value.
   */
  public final double getValue() {
    return value;
  }

  @Override
  public boolean equals(Object object) {
    return object != null && (super.equals(object) || (object instanceof Transaction &&
      ((Transaction) object).type.equals(type) && ((Transaction) object).name.equals(name)));
  }

  @Override
  public int hashCode() {
    return (type + name).hashCode();
  }

  @Override
  public String toString() {
    return Transaction.class.getSimpleName() + ":{type='" + type + "',name='" + name + "',date='"
      + date + "'requestType=" + requestType + ",currency='" + currency + "',value=" + value + "}";
  }

  /**
   * Transaction request type enumeration.
   */
  @Retention(RetentionPolicy.SOURCE)
  @IntDef({ RequestType.CREDIT, RequestType.DEBIT })
  public @interface RequestType {
    int CREDIT = 0;
    int DEBIT = 1;
  }
}
