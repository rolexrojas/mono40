package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.tpago.movil.Partner;
import com.tpago.movil.d.domain.util.StringUtils;
import com.tpago.movil.text.Texts;

/**
 * @author hecvasro
 */
public class BillRecipient extends Recipient {
  private final Partner partner;
  private final String contractNumber;
  private final String currency = "RD$";

  private BillBalance balance;

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
    return Texts.join("-", getType(), getPartner().getId(), getContractNumber());
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

  public enum Option {
    @SerializedName("PayTotal") TOTAL,
    @SerializedName("PayMinimum") MINIMUM
  }
}
