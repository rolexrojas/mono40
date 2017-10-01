package com.tpago.movil.data.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.domain.Email;
import com.tpago.movil.domain.Password;
import com.tpago.movil.domain.PhoneNumber;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ApiSignInBody {

  public static TypeAdapter<ApiSignInBody> typeAdapter(Gson gson) {
    return new AutoValue_ApiSignInBody.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_ApiSignInBody.Builder();
  }

  ApiSignInBody() {
  }

  @SerializedName("msisdn")
  abstract PhoneNumber phoneNumber();

  @SerializedName("username")
  abstract Email email();

  @SerializedName("password")
  abstract Password password();

  @SerializedName("imei")
  abstract String deviceId();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder phoneNumber(PhoneNumber phoneNumber);

    public abstract Builder email(Email email);

    public abstract Builder password(Password password);

    public abstract Builder deviceId(String deviceId);

    public abstract ApiSignInBody build();
  }
}
