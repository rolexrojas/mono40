package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Abstract product representation.
 *
 * @author hecvasro
 */
public abstract class Product implements Serializable {
  /**
   * Product's {@link ProductCategory category}.
   */
  private final ProductCategory category;
  /**
   * Product's {@link ProductType type}.
   */
  private final ProductType type;
  /**
   * Product's alias.
   */
  private final String alias;
  /**
   * Product's number.
   */
  private final String number;
  /**
   * Product's {@link Bank holder}.
   */
  private final Bank bank;
  /**
   * Product's amount.
   */
  private final String currency;
  /**
   * Indicates whether can be used as payment option or not.
   */
  private final boolean paymentOption;
  /**
   * Cost of querying the balance.
   */
  private BigDecimal queryFee;

  /**
   * Constructs a new account.
   *
   * @param category
   *   Product's {@link ProductCategory category}.
   * @param type
   *   Product's {@link ProductType type}.
   * @param alias
   *   Product's type.
   * @param number
   *   Product's number.
   * @param currency
   *   Product's amount.
   * @param bank
   *   Product's {@link Bank holder}.
   * @param queryFee
   *   Cost of querying the balance.
   * @param paymentOption
   *   Indicates whether can be used as a payment option or not.
   */
  Product(@NonNull ProductCategory category, @NonNull ProductType type, @NonNull String alias,
    @NonNull String number, @NonNull Bank bank, @NonNull String currency,
    @NonNull BigDecimal queryFee, boolean paymentOption) {
    this.category = category;
    this.type = type;
    this.alias = alias;
    this.number = number;
    this.bank = bank;
    this.currency = currency;
    this.queryFee = queryFee;
    this.paymentOption = paymentOption;
  }

  /**
   * Checks if the given {@link Product product} can be used as a payment option.
   *
   * @param product
   *   {@link Product} that will be checked.
   *
   * @return True if it can be used as a payment option, false otherwise.
   */
  public static boolean isPaymentOption(@NonNull Product product) {
    return (product.category.equals(ProductCategory.ACCOUNT)
      || product.category.equals(ProductCategory.CREDIT_CARD)) && product.paymentOption;
  }

  /**
   * TODO
   *
   * @param product
   *   TODO
   *
   * @return TODO
   */
  public static boolean isDefaultPaymentOption(@NonNull Product product) {
    return isPaymentOption(product);
  }

  /**
   * Gets the {@link ProductCategory category} of the product.
   *
   * @return Product's {@link ProductCategory category}.
   */
  @NonNull
  public final ProductCategory getCategory() {
    return category;
  }

  /**
   * Gets the {@link ProductType type} of the product.
   *
   * @return Product's {@link ProductType type}.
   */
  @NonNull
  public final ProductType getType() {
    return type;
  }

  /**
   * Gets the type of the product.
   *
   * @return Product's alias.
   */
  @NonNull
  public final String getAlias() {
    return alias;
  }

  /**
   * Gets the number of the product.
   *
   * @return Product's number.
   */
  @NonNull
  public final String getNumber() {
    return number;
  }

  /**
   * Gets the {@link Bank holder} of the product.
   *
   * @return Product's {@link Bank holder}.
   */
  @NonNull
  public final Bank getBank() {
    return bank;
  }

  /**
   * Gets the currency of the product.
   *
   * @return Product's currency.
   */
  @NonNull
  public final String getCurrency() {
    return currency;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final String getIdentifier() {
    return String.format("%1$s %2$s", bank.getName(), type);
  }

  /**
   * Gets the cost of querying the balance of the product.
   *
   * @return Cost of querying the balance.
   */
  @NonNull
  public BigDecimal getQueryFee() {
    return queryFee;
  }

  /**
   * Sets the cost of querying the balance of the product.
   *
   * @param queryFee
   *   Cost of querying the balance.
   */
  public void setQueryFee(@NonNull BigDecimal queryFee) {
    if (queryFee.compareTo(BigDecimal.ZERO) < 0) {
      queryFee = BigDecimal.ZERO;
    }
    this.queryFee = queryFee;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof Product
      && ((Product) object).category.equals(category)
      && ((Product) object).type.equals(type) && ((Product) object).alias.equals(alias))
      && ((Product) object).bank.equals(bank);
  }

  @Override
  public int hashCode() {
    return Utils.hashCode(category, type, alias, bank);
  }

  @Override
  public String toString() {
    return Product.class.getSimpleName() + ":{category='" + category + "', type='"
      + type + "',alias='" + alias + "',number='" + number + "',currency='" + currency
      + "',bank=" + bank + ",queryFee=" + queryFee + "}";
  }
}
