package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.Bank;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.text.Texts;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Abstract product representation.
 *
 * @author hecvasro
 */
@Deprecated
public class Product implements Serializable {
  public static boolean checkIfCreditCard(Product product) {
    return product.getType().equals(ProductType.CC) || product.getType().equals(ProductType.AMEX);
  }

  public static boolean checkIfLoan(Product product) {
    return product.getType().equals(ProductType.LOAN);
  }

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
   * Indicates whether is the default payment option or not.
   */
  private boolean isDefault;

  /**
   * Cost of querying the balance.
   */
  private BigDecimal queryFee;

  private String imageUriTemplate;

  /**
   * Constructs a new account.
   *
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
  Product(
    @NonNull ProductType type,
    @NonNull String alias,
    @NonNull String number,
    @NonNull Bank bank,
    @NonNull String currency,
    @NonNull BigDecimal queryFee,
    boolean paymentOption,
    boolean isDefault) {
    this.type = type;
    this.alias = alias;
    this.number = number;
    this.bank = bank;
    this.currency = currency;
    this.queryFee = queryFee;
    this.paymentOption = paymentOption;
    this.isDefault = isDefault;
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
    return !checkIfLoan(product) && product.paymentOption;
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
    return isPaymentOption(product) && product.isDefault;
  }

  public final String getId() {
    return Texts.join("-", bank.getId(), type, alias, number, currency);
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
    return String.format("%1$s %2$s", Bank.getName(bank), type);
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

  public String getImageUriTemplate() {
    return imageUriTemplate;
  }

  public void setImageUriTemplate(String imageUriTemplate) {
    this.imageUriTemplate = imageUriTemplate;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof Product
      && ((Product) object).type.equals(type) && ((Product) object).alias.equals(alias))
      && ((Product) object).bank.equals(bank);
  }

  @Override
  public int hashCode() {
    return getId().hashCode();
  }

  @Override
  public String toString() {
    return Product.class.getSimpleName() + ":{type='"
      + type + "',alias='" + alias + "',number='" + number + "',currency='" + currency
      + "',bank=" + bank + ",queryFee=" + queryFee + "}";
  }
}
