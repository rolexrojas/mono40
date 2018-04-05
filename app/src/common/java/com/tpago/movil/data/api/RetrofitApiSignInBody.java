package com.tpago.movil.data.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.Email;
import com.tpago.movil.lib.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class RetrofitApiSignInBody {

  public static TypeAdapter<RetrofitApiSignInBody> typeAdapter(Gson gson) {
    return new AutoValue_RetrofitApiSignInBody.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_RetrofitApiSignInBody.Builder();
  }

  RetrofitApiSignInBody() {
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
  public static abstract class Builder {

    Builder() {
    }

    abstract Builder phoneNumber(String phoneNumber);

    public final Builder phoneNumber(PhoneNumber phoneNumber) {
      ObjectHelper.checkNotNull(phoneNumber, "phoneNumber");

      return this.phoneNumber(phoneNumber.value());
    }

    abstract Builder email(String email);

    public final Builder email(Email email) {
      ObjectHelper.checkNotNull(email, "email");

      return this.email(email.value());
    }

    abstract Builder password(String password);

    public final Builder password(Password password) {
      ObjectHelper.checkNotNull(password, "password");

      return this.password(password.value());
    }

    public abstract Builder deviceId(String deviceId);

    public abstract RetrofitApiSignInBody build();
  }
}
