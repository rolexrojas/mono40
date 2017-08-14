package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.Partner;
import com.tpago.movil.d.domain.ProductInfo;
import java.math.BigDecimal;

/**
 * @author Hector Vasquez
 */
@AutoValue
public abstract class RechargeRequestBody {

  public static TypeAdapter<RechargeRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_RechargeRequestBody.GsonTypeAdapter(gson);
  }

  public static Builder createBuilder() {
    return new AutoValue_RechargeRequestBody.Builder();
  }

  @SerializedName("telco")
  public abstract Partner carrier();

  @SerializedName("recharge-msisdn")
  public abstract String phoneNumber();

  @SerializedName("funding-account")
  public abstract ProductInfo fundingAccount();

  @SerializedName("amount")
  public abstract BigDecimal amount();

  @SerializedName("pin")
  public abstract String pin();

  @AutoValue.Builder
  public static abstract class Builder {

    public abstract Builder carrier(Partner carrier);

    public abstract Builder phoneNumber(String phoneNumber);

    public abstract Builder fundingAccount(ProductInfo fundingAccount);

    public abstract Builder amount(BigDecimal amount);

    public abstract Builder pin(String pin);

    public abstract RechargeRequestBody build();
  }
}
