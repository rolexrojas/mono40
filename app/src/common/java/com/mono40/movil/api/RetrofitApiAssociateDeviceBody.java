package com.mono40.movil.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.Email;
import com.mono40.movil.data.api.DeviceInformationBody;
import com.mono40.movil.lib.Password;
import com.mono40.movil.PhoneNumber;
import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class RetrofitApiAssociateDeviceBody {

  public static TypeAdapter<RetrofitApiAssociateDeviceBody> typeAdapter(Gson gson) {
    return new AutoValue_RetrofitApiAssociateDeviceBody.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_RetrofitApiAssociateDeviceBody.Builder();
  }

  RetrofitApiAssociateDeviceBody() {
  }

  @SerializedName("msisdn")
  abstract String phoneNumber();

  @SerializedName("username")
  abstract String email();

  @SerializedName("password")
  abstract String password();

  @SerializedName("new-imei")
  abstract String deviceId();

  @SerializedName("device-information")
  abstract DeviceInformationBody deviceInformationBody();

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

    public abstract Builder deviceInformationBody(DeviceInformationBody deviceInformationBody);

    public abstract Builder deviceId(String deviceId);

    public abstract RetrofitApiAssociateDeviceBody build();
  }
}
