package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.Partner;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.text.Texts;

/**
 * @author hecvasro
 */
public class BillRecipient extends Recipient {
  private final Partner partner;
  private final String contractNumber;

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

  @Override
  public String getId() {
    return Texts.join("-", getType(), getPartner().getId(), getContractNumber());
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return partner.getName();
  }

  @Override
  public String toString() {
    return PhoneNumberRecipient.class.getSimpleName() + ":{super=" + super.toString()
      + ",partner=" + partner
      + ",contractNumber='" + contractNumber + "'"
      + "}";
  }
}
