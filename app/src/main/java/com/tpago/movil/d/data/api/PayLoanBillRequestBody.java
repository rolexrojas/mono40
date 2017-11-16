package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.d.domain.LoanBillBalance;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductInfo;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class PayLoanBillRequestBody {
  public static TypeAdapter<PayLoanBillRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_PayLoanBillRequestBody.GsonTypeAdapter(gson);
  }

  @SerializedName("amount-to-pay") public abstract BigDecimal amountToPay();
  @SerializedName("pay-option") public abstract LoanBillBalance.Option payOption();
  @SerializedName("pin") public abstract String pin();
  @SerializedName("funding-account") public abstract ProductInfo fundingAccount();
  @SerializedName("loan") public abstract Product loan();

  @AutoValue.Builder
  public static abstract class Builder {
    public static Builder create() {
      return new AutoValue_PayLoanBillRequestBody.Builder();
    }

    public abstract Builder amountToPay(BigDecimal amountToPay);
    public abstract Builder payOption(LoanBillBalance.Option payOption);
    public abstract Builder pin(String pin);
    public abstract Builder fundingAccount(ProductInfo fundingAccount);
    public abstract Builder loan(Product product);

    public abstract PayLoanBillRequestBody build();
  }
}
