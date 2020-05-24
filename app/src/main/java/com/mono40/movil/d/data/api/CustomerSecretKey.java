package com.mono40.movil.d.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class CustomerSecretKey {
    public static TypeAdapter<CustomerSecretKey> typeAdapter(Gson gson) {
        return new AutoValue_CustomerSecretKey.GsonTypeAdapter(gson);
    }

    @SerializedName("key")
    public abstract String key();
}
