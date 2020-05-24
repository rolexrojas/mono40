package com.mono40.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class ApiSecretTokenRequest {

    public static TypeAdapter<ApiSecretTokenRequest> typeAdapter(Gson gson) {
        return new AutoValue_ApiSecretTokenRequest.GsonTypeAdapter(gson);
    }

    public abstract ApiSecretMerchant merchant();
}
