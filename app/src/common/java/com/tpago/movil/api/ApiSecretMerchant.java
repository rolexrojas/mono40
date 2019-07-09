package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class ApiSecretMerchant {

    public static TypeAdapter<ApiSecretMerchant> typeAdapter(Gson gson) {
        return new AutoValue_ApiSecretMerchant.GsonTypeAdapter(gson);
    }

    public abstract int id();
    public abstract String description();
    public abstract String logo();
}
