package com.tpago.movil.session;

import com.google.auto.value.AutoValue;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.insurance.micro.MicroInsurancePartner;
import com.tpago.movil.paypal.PayPalAccount;
import com.tpago.movil.product.Products;
import com.tpago.movil.product.disbursable.DisbursableProduct;

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
