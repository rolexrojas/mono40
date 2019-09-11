package com.tpago.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@Deprecated
@AutoValue
public abstract class CustomerSecretTokenResponse {
    public static TypeAdapter<CustomerSecretTokenResponse> typeAdapter(Gson gson) {
        return new AutoValue_CustomerSecretTokenResponse.GsonTypeAdapter(gson);
    }

    @SerializedName("token")
    public abstract String token();
}
