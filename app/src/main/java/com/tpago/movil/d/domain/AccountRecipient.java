package com.tpago.movil.d.domain;

import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.d.domain.util.StringUtils;
import com.tpago.movil.util.digit.DigitUtil;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import java.util.Arrays;

import static com.tpago.movil.d.domain.RecipientType.ACCOUNT;

/**
 * @author hecvasro
 */
@Deprecated
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
    this.number = DigitUtil.removeNonDigits(number);
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
    if (ObjectHelper.isNotNull(this.product)) {
      this.bank = this.product.getBank();
    }
  }

  public final boolean canAcceptTransfers() {
    return ObjectHelper.isNotNull(this.bank) && ObjectHelper.isNotNull(this.product);
  }

  @Override
  public String toString() {
    return AccountRecipient.class.getSimpleName()
      + "{"
      + "\"bank\"=" + this.bank.toString()
      + ","
      + "\"number\"=" + this.number
      + ","
      + "\"product\"=" + this.product
      + "}";
  }

  @Override
  public String getId() {
    return StringHelper.join("-", Arrays.asList(this.getType(), this.number));
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
      || (ObjectHelper.isNotNull(this.bank) && StringUtils.matches(this.bank.name(), query))
      || (ObjectHelper.isNotNull(this.product) && StringUtils.matches(
      this.product.getAlias(),
      query
    ));
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
      this.label = StringHelper.nullIfEmpty(label);
      return this;
    }

    public final AccountRecipient build() {
      return new AccountRecipient(this);
    }
  }
}
