package com.tpago.movil.d.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.tpago.movil.dep.text.Texts;
import com.tpago.movil.util.DigitHelper;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import java.math.BigDecimal;
import java.util.Comparator;

import io.reactivex.Observable;

/**
 * Abstract creditCard representation.
 *
 * @author hecvasro
 */
@Deprecated
public class Product implements Parcelable {

  static String numberMasked(String number, int count) {
    return Observable.range(1, count)
      .map((index) -> "••••")
      .concatWith(Observable.just(number))
      .toList()
      .map((list) -> StringHelper.join(" ", list))
      .blockingGet();
  }

  protected Product(Parcel in) {
    type = ProductType.valueOf(in.readString());
    alias = in.readString();
    number = in.readString();
    bank = in.readParcelable(Bank.class.getClassLoader());
    currency = in.readString();
    paymentOption = in.readByte() != 0;
    isDefault = in.readByte() != 0;
    imageUriTemplate = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(type.name());
    dest.writeString(alias);
    dest.writeString(number);
    dest.writeParcelable(bank, flags);
    dest.writeString(currency);
    dest.writeByte((byte) (paymentOption ? 1 : 0));
    dest.writeByte((byte) (isDefault ? 1 : 0));
    dest.writeString(imageUriTemplate);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Product> CREATOR = new Creator<Product>() {
    @Override
    public Product createFromParcel(Parcel in) {
      return new Product(in);
    }

    @Override
    public Product[] newArray(int size) {
      return new Product[size];
    }
  };

  public static boolean checkIfAccount(Product product) {
    return !checkIfCreditCard(product) && !checkIfLoan(product);
  }

  public static boolean checkIfCreditCard(Product product) {
    return product.getType()
      .equals(ProductType.CC) || product.getType()
      .equals(ProductType.AMEX);
  }

  public static boolean checkIfLoan(Product product) {
    return product.getType()
      .equals(ProductType.LOAN);
  }

  public static Comparator<Product> comparator() {
    return new Comparator<Product>() {
      @Override
      public int compare(Product pa, Product pb) {
        final int r = pa.getBank()
          .getName()
          .compareTo(pb.getBank()
            .getName());
        if (r == 0) {
          return pa.getAlias()
            .compareTo(pb.getAlias());
        } else {
          return r;
        }
      }
    };
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
    boolean isDefault
  ) {
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
   * Checks if the given {@link Product creditCard} can be used as a payment option.
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
   * Gets the {@link ProductType type} of the creditCard.
   *
   * @return Product's {@link ProductType type}.
   */
  @NonNull
  public final ProductType getType() {
    return type;
  }

  /**
   * Gets the type of the creditCard.
   *
   * @return Product's alias.
   */
  @NonNull
  public final String getAlias() {
    return alias;
  }

  /**
   * Gets the number of the creditCard.
   *
   * @return Product's number.
   */
  @NonNull
  public final String getNumber() {
    return number;
  }

  public final String getNumberSanitized() {
    return DigitHelper.getLast4Digits(this.getNumber());
  }

  public final String getNumberMasked() {
    return numberMasked(this.getNumberSanitized(), checkIfCreditCard(this) ? 3 : 2);
  }

  /**
   * Gets the {@link Bank holder} of the creditCard.
   *
   * @return Product's {@link Bank holder}.
   */
  @NonNull
  public final Bank getBank() {
    return bank;
  }

  /**
   * Gets the currency of the creditCard.
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
    return String.format("%1$s %2$s", Banks.getName(bank), type);
  }

  /**
   * Gets the cost of querying the balance of the creditCard.
   *
   * @return Cost of querying the balance.
   */
  @NonNull
  public BigDecimal getQueryFee() {
    if (this.queryFee == null) {
      this.queryFee = BigDecimal.ZERO;
    }
    return queryFee;
  }

  /**
   * Sets the cost of querying the balance of the creditCard.
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
    return super.equals(object) || (ObjectHelper.isNotNull(object) && object instanceof Product
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
