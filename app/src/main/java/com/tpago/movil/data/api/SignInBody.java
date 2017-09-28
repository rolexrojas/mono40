package com.tpago.movil.data.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class SignInBody {

  public static TypeAdapter<SignInBody> typeAdapter(Gson gson) {
    return new AutoValue_SignInBody.GsonTypeAdapter(gson);
  }

  static Builder builder() {
    return new AutoValue_SignInBody.Builder();
  }

  SignInBody() {
  }

  @SerializedName("msisdn")
  abstract String phoneNumber();

  @SerializedName("username")
  abstract String email();

  @SerializedName("password")
  abstract String password();

  @SerializedName("imei")
  abstract String deviceId();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  @AutoValue.Builder
  static abstract class Builder {

    Builder() {
    }

    abstract Builder phoneNumber(String phoneNumber);

    abstract Builder email(String email);

    abstract Builder password(String password);

    abstract Builder deviceId(String deviceId);

    abstract SignInBody build();
  }
}
