package com.tpago.movil.d.domain;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.domain.Bank;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
public class NonAffiliatedPhoneNumberRecipient extends Recipient {
  public static final Creator<NonAffiliatedPhoneNumberRecipient> CREATOR
    = new Creator<NonAffiliatedPhoneNumberRecipient>() {
    @Override
    public NonAffiliatedPhoneNumberRecipient createFromParcel(Parcel source) {
      return new NonAffiliatedPhoneNumberRecipient(source);
    }

    @Override
    public NonAffiliatedPhoneNumberRecipient[] newArray(int size) {
      return new NonAffiliatedPhoneNumberRecipient[size];
    }
  };

  private final String phoneNumber;

  private Bank bank;
  private String accountNumber;
  private Product product;

  protected NonAffiliatedPhoneNumberRecipient(Parcel source) {
    super(source);
    phoneNumber = source.readString();
    bank = source.readParcelable(Bank.class.getClassLoader());
    accountNumber = source.readString();
    product = source.readParcelable(Product.class.getClassLoader());
  }

  public NonAffiliatedPhoneNumberRecipient(
    @NonNull String phoneNumber,
    @Nullable String label,
    Bank bank,
    String accountNumber,
    Product product) {
    super(RecipientType.NON_AFFILIATED_PHONE_NUMBER, label);
    this.phoneNumber = phoneNumber;
    this.bank = bank;
    this.accountNumber = accountNumber;
    this.product = product;
  }

  public NonAffiliatedPhoneNumberRecipient(@NonNull String phoneNumber, @Nullable String label) {
    this(phoneNumber, label, null, null, null);
  }

  public NonAffiliatedPhoneNumberRecipient(@NonNull String phoneNumber) {
    this(phoneNumber, null);
  }

  public final String getPhoneNumber() {
    return phoneNumber;
  }

  public boolean canBeTransferTo() {
    return Objects.checkIfNotNull(bank)
      && Texts.checkIfNotEmpty(accountNumber)
      && Objects.checkIfNotNull(product);
  }

  public Bank getBank() {
    return bank;
  }

  public void setBank(Bank bank) {
    this.bank = bank;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @Override
  public String toString() {
    return PhoneNumberRecipient.class.getSimpleName() + ":{super=" + super.toString()
      + ",phoneNumber='" + phoneNumber + "'"
      + ",bank=" + bank.toString()
      + ",accountNumber='" + accountNumber + "'"
      + ",product=" + product
      + "}";
  }

  @Override
  public String getId() {
    return Texts.join("-", getType(), phoneNumber, bank.getId(), accountNumber);
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return com.tpago.movil.PhoneNumber.format(phoneNumber);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(phoneNumber);
    dest.writeParcelable(bank, flags);
    dest.writeString(accountNumber);
    dest.writeParcelable(product, flags);
  }
}
