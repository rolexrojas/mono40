package com.mono40.movil.session;

import com.google.auto.value.AutoValue;
import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.insurance.micro.MicroInsurancePartner;
import com.mono40.movil.paypal.PayPalAccount;
import com.mono40.movil.product.Products;
import com.mono40.movil.product.disbursable.DisbursableProduct;

import java.util.List;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class SessionData {

  public static Builder builder() {
    return new AutoValue_SessionData.Builder();
  }

  SessionData() {
  }

  public abstract List<Bank> banks();

  public abstract List<Partner> partners();

  public abstract Products products();

  public abstract List<MicroInsurancePartner> microInsurancePartners();

  public abstract List<PayPalAccount> payPalAccounts();

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder banks(List<Bank> banks);

    public abstract Builder partners(List<Partner> partners);

    public abstract Builder products(Products products);

    public abstract Builder microInsurancePartners(List<MicroInsurancePartner> microInsurancePartners);

    public abstract Builder payPalAccounts(List<PayPalAccount> payPalAccounts);

    public abstract SessionData build();
  }
}
