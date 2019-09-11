package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class ApiSecretTokenResponse {

    public static TypeAdapter<ApiSecretTokenResponse> typeAdapter(Gson gson) {
        return new AutoValue_ApiSecretTokenResponse.GsonTypeAdapter(gson);
    }

    public abstract String token();
}
