package com.tpago.movil.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ApiError {
  public static ApiError create(Code code, String description) {
    return new AutoValue_ApiError(code, description);
  }

  public static TypeAdapter<ApiError> typeAdapter(Gson gson) {
    return new AutoValue_ApiError.GsonTypeAdapter(gson);
  }

  public abstract Code getCode();

  public abstract String getDescription();

  public enum Code {
    @SerializedName("0004") INCORRECT_USERNAME_AND_PASSWORD,
    @SerializedName("0005") INVALID_EMAIL,
    @SerializedName("0009") INVALID_DEVICE_ID,
    @SerializedName("0012") ALREADY_ASSOCIATED_PROFILE,
    @SerializedName("0013") INVALID_PHONE_NUMBER,
    @SerializedName("0014") UNAVAILABLE_SERVICE,
    @SerializedName("0016") INVALID_PIN,
    @SerializedName("0021") ALREADY_REGISTERED_USERNAME,
    @SerializedName("0055") ALREADY_REGISTERED_EMAIL,
    @SerializedName("0112") ALREADY_ASSOCIATED_DEVICE,
    @SerializedName("0144") UNASSOCIATED_PROFILE,
    @SerializedName("0216") UNASSOCIATED_PHONE_NUMBER
  }
}
