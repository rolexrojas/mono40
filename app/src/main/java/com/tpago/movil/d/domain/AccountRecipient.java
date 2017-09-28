package com.tpago.movil.d.domain;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.tpago.movil.d.domain.util.StringUtils;

import static com.google.common.base.Strings.emptyToNull;
import static com.tpago.movil.DigitHelper.removeNonDigits;
import static com.tpago.movil.d.domain.RecipientType.ACCOUNT;
import static com.tpago.movil.util.ObjectHelper.isNotNull;

/**
 * @author hecvasro
 */
public final class AccountRecipient extends Recipient {

  public static final Creator<AccountRecipient> CREATOR = new Creator<AccountRecipient>() {
    @Override
    public AccountRecipient createFromParcel(Parcel source) {
      return new AccountRecipient(source);
    }

    @Override
    public AccountRecipient[] newArray(int size) {
      return new AccountRecipient[size];
    }
  };

  public static Builder builder() {
    return new Builder();
  }

  private Bank bank;
  private String number;

  private Product product;

  private AccountRecipient(Builder builder) {
    super(ACCOUNT, builder.label);

    this.bank = builder.bank;
    this.number = builder.number;

    this.product = builder.product;
  }

  private AccountRecipient(Parcel source) {
    super(source);

    this.bank = source.readParcelable(Bank.class.getClassLoader());
    this.number = source.readString();
    this.product = source.readParcelable(Product.class.getClassLoader());
  }

  public final String number() {
    return this.number;
  }

  public final void number(String number) {
    this.number = removeNonDigits(number);
  }

  public final Bank bank() {
    return this.bank;
  }

  public final void bank(Bank bank) {
    this.bank = bank;
  }

  public final Product product() {
    return this.product;
  }

  public final void product(Product product) {
    this.product = product;
    if (isNotNull(this.product)) {
      this.bank = this.product.getBank();
    }
  }

  public final boolean canAcceptTransfers() {
    return isNotNull(this.bank) && isNotNull(this.product);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("bank", this.bank)
      .add("number", this.number)
      .add("product", this.product)
      .toString();
  }

  @Override
  public String getId() {
    return Joiner.on('-')
      .join(this.getType(), this.number);
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return this.number;
  }

  @Override
  public boolean matches(@Nullable String query) {
    return super.matches(query)
      || StringUtils.matches(this.number, query)
      || (isNotNull(this.bank) && StringUtils.matches(this.bank.getName(), query))
      || (isNotNull(this.product) && StringUtils.matches(this.product.getAlias(), query));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);

    dest.writeParcelable(this.bank, flags);
    dest.writeString(this.number);
    dest.writeParcelable(this.product, flags);
  }

  public static final class Builder {

    private Bank bank;
    private String number;

    private Product product;

    private String label;

    private Builder() {
    }

    public final Builder bank(Bank bank) {
      this.bank = bank;
      return this;
    }

    public final Builder number(String number) {
      this.number = number;
      return this;
    }

    public final Builder product(Product product) {
      this.product = product;
      return this;
    }

    public final Builder label(String label) {
      this.label = emptyToNull(label);
      return this;
    }

    public final AccountRecipient build() {
      return new AccountRecipient(this);
    }
  }
}
