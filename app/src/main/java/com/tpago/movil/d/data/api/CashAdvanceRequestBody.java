package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.d.domain.ProductInfo;
import java.math.BigDecimal;

/**
 * @author Hector Vasquez
 */
@AutoValue
public abstract class CashAdvanceRequestBody {

  public static TypeAdapter<CashAdvanceRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_CashAdvanceRequestBody.GsonTypeAdapter(gson);
  }

  public static Builder createBuilder() {
    return new AutoValue_CashAdvanceRequestBody.Builder();
  }

  @SerializedName("funding-account")
  public abstract ProductInfo fundingAccount();

  @SerializedName("recipient-account")
  public abstract ProductInfo recipientAccount();

  @SerializedName("amount")
  public abstract BigDecimal amount();

  @SerializedName("pin")
  public abstract String pin();

  @AutoValue.Builder
  public static abstract class Builder {

    public abstract Builder fundingAccount(ProductInfo fundingAccount);

    public abstract Builder recipientAccount(ProductInfo recipientAccount);

    public abstract Builder amount(BigDecimal amount);

    public abstract Builder pin(String pin);

    public abstract CashAdvanceRequestBody build();
  }
}
