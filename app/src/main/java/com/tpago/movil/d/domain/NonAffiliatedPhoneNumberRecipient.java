package com.tpago.movil.d.domain;

import static com.tpago.movil.d.domain.RecipientType.NON_AFFILIATED_PHONE_NUMBER;
import static com.tpago.movil.text.Texts.checkIfNotEmpty;
import static com.tpago.movil.util.Objects.checkIfNotNull;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.PhoneNumber;
import com.tpago.movil.Partner;
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

  private final PhoneNumber phoneNumber;

  private Bank bank;
  private String accountNumber;
  private Product product;
  private Partner carrier;

  protected NonAffiliatedPhoneNumberRecipient(Parcel source) {
    super(source);

    this.phoneNumber = source.readParcelable(PhoneNumber.class.getClassLoader());
    this.bank = source.readParcelable(Bank.class.getClassLoader());
    this.accountNumber = source.readString();
    this.product = source.readParcelable(Product.class.getClassLoader());
    this.carrier = source.readParcelable(Partner.class.getClassLoader());
  }

  public NonAffiliatedPhoneNumberRecipient(
    @NonNull PhoneNumber phoneNumber,
    @Nullable String label,
    Bank bank,
    String accountNumber,
    Product product,
    Partner carrier
  ) {
    super(NON_AFFILIATED_PHONE_NUMBER, label);

    this.phoneNumber = phoneNumber;
    this.bank = bank;
    this.accountNumber = accountNumber;
    this.product = product;
    this.carrier = carrier;
  }

  public NonAffiliatedPhoneNumberRecipient(@NonNull PhoneNumber phoneNumber,
    @Nullable String label) {
    this(phoneNumber, label, null, null, null, null);
  }

  public NonAffiliatedPhoneNumberRecipient(@NonNull PhoneNumber phoneNumber) {
    this(phoneNumber, null);
  }

  public final PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  public final boolean canAcceptTransfers() {
    return checkIfNotNull(this.bank) && checkIfNotEmpty(this.accountNumber) && checkIfNotNull(this.product);
  }

  public final boolean canAcceptRecharges() {
    return checkIfNotNull(carrier);
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

  public final void setCarrier(Partner carrier) {
    this.carrier = carrier;
  }

  public final Partner getCarrier() {
    return this.carrier;
  }

  @Override
  public String toString() {
    return PhoneNumberRecipient.class.getSimpleName() + ":{super=" + super.toString()
      + ",phoneNumber='" + this.phoneNumber + "'"
      + ",bank=" + this.bank.toString()
      + ",accountNumber='" + this.accountNumber + "'"
      + ",product=" + this.product.toString()
      + ",carrier=" + this.carrier.toString()
      + "}";
  }

  @Override
  public String getId() {
    return Texts.join("-", this.getType(), this.phoneNumber.value());
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return this.phoneNumber.formattedValued();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);

    dest.writeParcelable(this.phoneNumber, flags);
    dest.writeParcelable(this.bank, flags);
    dest.writeString(this.accountNumber);
    dest.writeParcelable(this.product, flags);
    dest.writeParcelable(this.carrier, flags);
  }
}
