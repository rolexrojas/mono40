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
@Deprecated
@AutoValue
public abstract class TransferToMerchantRequestBody {
  public static TypeAdapter<TransferToMerchantRequestBody> typeAdapter(Gson gson) {
    return new AutoValue_TransferToMerchantRequestBody.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_TransferToMerchantRequestBody.Builder();
  }

  @SerializedName("amount") public abstract BigDecimal amount();
  @SerializedName("funding-account") public abstract Product fundingProduct();
  @SerializedName("pin") public abstract String pin();

  @AutoValue.Builder
  public static abstract class Builder {
    @SerializedName("amount") public abstract Builder amount(BigDecimal amount);
    @SerializedName("funding-account") public abstract Builder fundingProduct(Product fundingProduct);
    @SerializedName("pin") public abstract Builder pin(String pin);

    public abstract TransferToMerchantRequestBody build();
  }
}
