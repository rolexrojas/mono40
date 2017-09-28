package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.d.domain.CreditCardBillBalance;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductInfo;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class PayCreditCardBillRequestBody {
  public static TypeAdapter<PayCreditCardBillRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_PayCreditCardBillRequestBody.GsonTypeAdapter(gson);
  }

  @SerializedName("amount-to-pay") public abstract BigDecimal amountToPay();
  @SerializedName("pay-option") public abstract CreditCardBillBalance.Option payOption();
  @SerializedName("pin") public abstract String pin();
  @SerializedName("funding-account") public abstract ProductInfo fundingAccount();
  @SerializedName("credit-card") public abstract Product creditCard();

  @AutoValue.Builder
  public static abstract class Builder {
    public static Builder create() {
      return new AutoValue_PayCreditCardBillRequestBody.Builder();
    }

    public abstract Builder amountToPay(BigDecimal amountToPay);
    public abstract Builder payOption(CreditCardBillBalance.Option payOption);
    public abstract Builder pin(String pin);
    public abstract Builder fundingAccount(ProductInfo fundingAccount);
    public abstract Builder creditCard(Product product);

    public abstract PayCreditCardBillRequestBody build();
  }
}
