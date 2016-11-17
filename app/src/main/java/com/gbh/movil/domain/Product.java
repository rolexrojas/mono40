package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;

import java.math.BigDecimal;

/**
 * Abstract product representation.
 *
 * @author hecvasro
 */
public abstract class Product {
  /**
   * Product's {@link ProductCategory category}.
   */
  private final ProductCategory category;
  /**
   * Product's {@link ProductIdentifier identifier}.
   */
  private final ProductIdentifier identifier;
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
   * Cost of querying the balance.
   */
  private BigDecimal queryFee;

  /**
   * Constructs a new account.
   *
   * @param category
   *   Product's {@link ProductCategory category}.
   * @param identifier
   *   Product's {@link ProductIdentifier identifier}.
   * @param alias
   *   Product's identifier.
   * @param number
   *   Product's number.
   * @param currency
   *   Product's amount.
   * @param bank
   *   Product's {@link Bank holder}.
   * @param queryFee
   *   Cost of querying the balance.
   */
  Product(@NonNull ProductCategory category, @NonNull ProductIdentifier identifier, @NonNull String alias,
    @NonNull String number, @NonNull Bank bank, @NonNull String currency,
    @NonNull BigDecimal queryFee) {
    this.category = category;
    this.identifier = identifier;
    this.alias = alias;
    this.number = number;
    this.bank = bank;
    this.currency = currency;
    this.queryFee = queryFee;
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
   * Gets the {@link ProductIdentifier identifier} of the product.
   *
   * @return Product's {@link ProductIdentifier identifier}.
   */
  @NonNull
  public final ProductIdentifier getIdentifier() {
    return identifier;
  }

  /**
   * Gets the identifier of the product.
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
      && ((Product) object).category.equals(category) && ((Product) object).identifier.equals(identifier)
      && ((Product) object).alias.equals(alias)) && ((Product) object).bank.equals(bank);
  }

  @Override
  public int hashCode() {
    return Utils.hashCode(identifier, alias);
  }

  @Override
  public String toString() {
    return Product.class.getSimpleName() + ":{category='" + category + "', identifier='"
      + identifier + "',alias='" + alias + "',number='" + number + "',currency='" + currency
      + "',bank=" + bank + ",queryFee=" + queryFee + "}";
  }
}
