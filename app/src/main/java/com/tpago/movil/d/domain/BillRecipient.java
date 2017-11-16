package com.tpago.movil.d.domain;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.tpago.movil.dep.Partner;
import com.tpago.movil.d.domain.util.StringUtils;
import com.tpago.movil.dep.text.Texts;

/**
 * @author hecvasro
 */
@Deprecated
public class BillRecipient extends Recipient {

  public static final Creator<BillRecipient> CREATOR = new Creator<BillRecipient>() {
    @Override
    public BillRecipient createFromParcel(Parcel source) {
      return new BillRecipient(source);
    }

    @Override
    public BillRecipient[] newArray(int size) {
      return new BillRecipient[size];
    }
  };

  private final Partner partner;
  private final String contractNumber;
  private final String currency = "RD$";

  private BillBalance balance;

  protected BillRecipient(Parcel source) {
    super(source);
    partner = source.readParcelable(Partner.class.getClassLoader());
    contractNumber = source.readString();
    balance = source.readParcelable(BillBalance.class.getClassLoader());
  }

  public BillRecipient(Partner partner, String contractNumber, @Nullable String label) {
    super(RecipientType.BILL, label);
    this.partner = partner;
    this.contractNumber = contractNumber;
  }

  public BillRecipient(Partner partner, String contractNumber) {
    this(partner, contractNumber, null);
  }

  public final Partner getPartner() {
    return partner;
  }

  public final String getContractNumber() {
    return contractNumber;
  }

  public final String getCurrency() {
    return currency;
  }

  public final BillBalance getBalance() {
    return balance;
  }

  public final void setBalance(BillBalance balance) {
    this.balance = balance;
  }

  @Override
  public String getId() {
    return Texts.join(
      "-",
      this.getType(),
      this.getPartner()
        .getId(),
      this.getContractNumber()
    );
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return contractNumber;
  }

  @Nullable
  @Override
  public String getLabel() {
    String label = super.getLabel();
    if (Texts.checkIfEmpty(label)) {
      label = String.format("Factura de %1$s", partner.getName());
    }
    return label;
  }

  @Override
  public String toString() {
    return PhoneNumberRecipient.class.getSimpleName() + ":{super=" + super.toString()
      + ",partner=" + partner
      + ",contractNumber='" + contractNumber + "'"
      + ",balance=" + balance
      + "}";
  }

  @Override
  public boolean matches(@Nullable String query) {
    return super.matches(query)
      || StringUtils.matches(partner.getName(), query)
      || StringUtils.matches(contractNumber, query);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeParcelable(partner, flags);
    dest.writeString(contractNumber);
    dest.writeParcelable(balance, flags);
  }

  public enum Option {
    @SerializedName("PayTotal")TOTAL,
    @SerializedName("PayMinimum")MINIMUM
  }
}
