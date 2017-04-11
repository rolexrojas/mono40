package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductInfo;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class PayBillRequestBody {
  public static PayBillRequestBody create(
    Product fundingAccount,
    BillRecipient recipient,
    String pin,
    BillRecipient.Option payOption) {
    return new AutoValue_PayBillRequestBody(
      ProductInfo.create(fundingAccount),
      BillRequestBody.create(recipient.getPartner(), recipient.getContractNumber(), null),
      recipient.getBalance(),
      pin,
      payOption);
  }

  public static TypeAdapter<PayBillRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_PayBillRequestBody.GsonTypeAdapter(gson);
  }

  @SerializedName("funding-account") public abstract ProductInfo getFundingAccount();
  @SerializedName("invoice") public abstract BillRequestBody getInvoice();
  @SerializedName("invoice-detail") public abstract BillBalance getInvoiceDetail();
  @SerializedName("pin") public abstract String getPin();
  @SerializedName("pay-option") public abstract BillRecipient.Option getPayOption();
}
