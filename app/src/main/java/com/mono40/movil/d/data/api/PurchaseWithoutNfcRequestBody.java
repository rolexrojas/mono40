package com.mono40.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.d.domain.Product;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class PurchaseWithoutNfcRequestBody {
    public static TypeAdapter<PurchaseWithoutNfcRequestBody> typeAdapter(Gson gson) {
        return new AutoValue_PurchaseWithoutNfcRequestBody.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_PurchaseWithoutNfcRequestBody.Builder();
    }

    @SerializedName("funding-account")
    public abstract Product fundingProduct();

    @SerializedName("pin")
    public abstract String pin();

    @AutoValue.Builder
    public static abstract class Builder {
        @SerializedName("funding-account")
        public abstract Builder fundingProduct(Product fundingProduct);

        @SerializedName("pin")
        public abstract Builder pin(String pin);

        public abstract PurchaseWithoutNfcRequestBody build();
    }
}
