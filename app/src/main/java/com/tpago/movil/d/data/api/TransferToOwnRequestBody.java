package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.d.domain.Product;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class TransferToOwnRequestBody {
  public static TypeAdapter<TransferToOwnRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_TransferToOwnRequestBody.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_TransferToOwnRequestBody.Builder();
  }

  @SerializedName("amount") public abstract BigDecimal amount();
  @SerializedName("funding-account") public abstract Product fundingProduct();
  @SerializedName("pin") public abstract String pin();
  @SerializedName("recipient-account") public abstract Product recipientProduct();

  @AutoValue.Builder
  public static abstract class Builder {
    @SerializedName("amount") public abstract Builder amount(BigDecimal amount);
    @SerializedName("funding-account") public abstract Builder fundingProduct(Product fundingProduct);
    @SerializedName("pin") public abstract Builder pin(String pin);
    @SerializedName("recipient-account") public abstract Builder recipientProduct(Product recipientProduct);

    public abstract TransferToOwnRequestBody build();
  }
}
