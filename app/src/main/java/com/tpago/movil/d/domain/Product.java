package com.tpago.movil.d.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.tpago.movil.Currency;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.dep.text.Texts;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ComparisonChain;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;
import com.tpago.movil.util.digit.DigitUtil;

import java.math.BigDecimal;

import io.reactivex.Observable;

/**
 * Abstract creditCard representation.
 *
 * @author hecvasro
 */
@Deprecated
public class Product implements Comparable<Product>, Parcelable {

  public static Builder builder() {
    return new Builder();
  }

  public static Product create(com.tpago.movil.product.Product product) {
    ObjectHelper.checkNotNull(product, "product");
    return builder()
      .bank(product.bank())
      .type(ProductType.valueOf(product.type()))
      .number(product.number())
      .alias(product.alias())
      .currency(
        product.currency()
          .value()
      )
      .queryFee(product.balanceQueryCost())
      .imageUriTemplate(product.imageTemplate())
      .paymentOption(product.isPaymentMethod())
      .isDefault(product.isPrimaryPaymentMethod())
      .altpanKey(product.altpanKey())
      .build();
  }

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
   * Key to activate the card
   */
  private String altpanKey;
  /**
   * Cost of querying the balance.
   */
  private BigDecimal queryFee;

  private String imageUriTemplate;

  private Product(Builder builder) {
    this.bank = builder.bank;
    this.type = builder.type;
    this.number = builder.number;
    this.alias = builder.alias;
    this.currency = builder.currency;
    this.queryFee = builder.queryFee;
    this.imageUriTemplate = builder.imageUriTemplate;
    this.paymentOption = builder.paymentOption;
    this.isDefault = builder.isDefault;
    this.altpanKey = builder.altpanKey;
  }

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
    boolean isDefault,
    String altpanKey
  ) {
    this.type = type;
    this.alias = alias;
    this.number = number;
    this.bank = bank;
    this.currency = currency;
    this.queryFee = queryFee;
    this.paymentOption = paymentOption;
    this.isDefault = isDefault;
    this.altpanKey = altpanKey;
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
    return Texts.join("-", bank.code(), type, alias, number, currency);
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
    return DigitUtil.getLast4Digits(this.getNumber());
  }

  final String getNumberLast4Digits() {
    return DigitUtil.getLast4Digits(this.getNumber());
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
   * Gets the currency of the creditCard.
   *
   * @return Product's currency.
   */
  public final String getAltpanKey() {
    return altpanKey;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final String getIdentifier() {
    return String.format("%1$s %2$s", bank.name(), type);
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
    return getId()
      .hashCode();
  }

  @Override
  public String toString() {
    return Product.class.getSimpleName()
      + ":{type='"
      + type
      + "',alias='"
      + alias
      + "',number='"
      + number
      + "',currency='"
      + currency
      + "',bank="
      + bank
      + ",queryFee="
      + queryFee
      + "}";
  }

  @Override
  public int compareTo(@NonNull Product that) {
    return ComparisonChain.create()
      .compare(this.bank, that.bank)
      .compare(this.alias, that.alias)
      .result();
  }

  public final com.tpago.movil.product.Product toProduct() {
    return com.tpago.movil.product.Product.builder()
      .bank(this.bank)
      .type(this.type.name())
      .number(this.number)
      .alias(this.alias)
      .currency(Currency.create(this.currency))
      .balanceQueryCost(this.queryFee)
      .imageTemplate(this.imageUriTemplate)
      .isPaymentMethod(this.paymentOption)
      .isPrimaryPaymentMethod(this.isDefault)
      .altpanKey(this.altpanKey)
      .build();
  }

  public static final class Builder {

    private Bank bank;
    private ProductType type;
    private String number;
    private String alias;
    private String currency;
    private BigDecimal queryFee;
    private String imageUriTemplate;
    private boolean paymentOption;
    private boolean isDefault;
    private String altpanKey;

    private Builder() {
    }

    public final Builder bank(Bank bank) {
      this.bank = ObjectHelper.checkNotNull(bank, "bank");
      return this;
    }

    public final Builder type(ProductType type) {
      this.type = ObjectHelper.checkNotNull(type, "type");
      return this;
    }

    public final Builder number(String number) {
      this.number = StringHelper.checkIsNotNullNorEmpty(number, "number");
      return this;
    }

    public final Builder alias(String alias) {
      this.alias = StringHelper.checkIsNotNullNorEmpty(alias, "alias");
      return this;
    }

    public final Builder currency(String currency) {
      this.currency = StringHelper.checkIsNotNullNorEmpty(currency, "currency");
      return this;
    }

    public final Builder queryFee(BigDecimal queryFee) {
      this.queryFee = ObjectHelper.checkNotNull(queryFee, "queryFee");
      return this;
    }

    public final Builder imageUriTemplate(String imageUriTemplate) {
      this.imageUriTemplate = StringHelper.nullIfEmpty(imageUriTemplate);
      return this;
    }

    public final Builder paymentOption(boolean paymentOption) {
      this.paymentOption = paymentOption;
      return this;
    }

    public final Builder isDefault(boolean isDefault) {
      this.isDefault = isDefault;
      return this;
    }

    public final Builder altpanKey(String altpanKey) {
      this.altpanKey = altpanKey;
      return this;
    }

    public final Product build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("bank", ObjectHelper.isNull(this.bank))
        .addPropertyNameIfMissing("type", ObjectHelper.isNull(this.type))
        .addPropertyNameIfMissing("number", StringHelper.isNullOrEmpty(this.number))
        .addPropertyNameIfMissing("alias", StringHelper.isNullOrEmpty(this.alias))
        .addPropertyNameIfMissing("currency", StringHelper.isNullOrEmpty(this.currency))
        .addPropertyNameIfMissing("queryFee", ObjectHelper.isNull(this.queryFee))
        .checkNoMissingProperties();
      return new Product(this);
    }
  }
}
