package com.mono40.movil.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.Name;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ApiName {

  public static TypeAdapter<ApiName> typeAdapter(Gson gson) {
    return new AutoValue_ApiName.GsonTypeAdapter(gson);
  }

  public static ApiName create(Name name) {
    return new AutoValue_ApiName(name.first(), name.last());
  }

  ApiName() {
  }

  @SerializedName("name")
  public abstract String firstName();

  @SerializedName("last-name")
  public abstract String lastName();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();
}
