package com.tpago.movil.api;

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

  @SerializedName("manufacturer")
  abstract String manufacturer();

  @SerializedName("serial")
  abstract String serial();

  @SerializedName("fingerprint")
  abstract String fingerprint();

  @SerializedName("device")
  abstract String device();

  @SerializedName("display")
  abstract String display();

  @SerializedName("board")
  abstract String board();

  @SerializedName("bootloader")
  abstract String bootloader();

  @SerializedName("brand")
  abstract String brand();

  @SerializedName("hardware")
  abstract String hardware();

  @SerializedName("model")
  abstract String model();

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

    public abstract Builder manufacturer(String manufacturer);
    public abstract Builder serial(String serial);
    public abstract Builder fingerprint(String fingerprint);
    public abstract Builder device(String device);
    public abstract Builder display(String display);
    public abstract Builder board(String board);
    public abstract Builder bootloader(String bootloader);
    public abstract Builder brand(String brand);
    public abstract Builder hardware(String hardware);
    public abstract Builder model(String model);

    public abstract Builder deviceId(String deviceId);

    public abstract RetrofitApiAssociateDeviceBody build();
  }
}
