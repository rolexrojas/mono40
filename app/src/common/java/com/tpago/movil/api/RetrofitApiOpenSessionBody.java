package com.tpago.movil.api;

import android.util.Base64;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.session.User;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class RetrofitApiOpenSessionBody {

  public static TypeAdapter<RetrofitApiOpenSessionBody> typeAdapter(Gson gson) {
    return new AutoValue_RetrofitApiOpenSessionBody.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_RetrofitApiOpenSessionBody.Builder();
  }

  RetrofitApiOpenSessionBody() {
  }

  @SerializedName("account-id")
  public abstract int userId();

  @SerializedName("msisdn")
  public abstract String userPhoneNumber();

  @SerializedName("imei")
  public abstract String deviceId();

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

  @SerializedName("public-key")
  public abstract String signedData();

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

    abstract Builder userId(int userId);

    abstract Builder userPhoneNumber(String userPhoneNumber);

    public final Builder user(User user) {
      ObjectHelper.checkNotNull(user, "user");

      return this.userId(user.id())
        .userPhoneNumber(
          user.phoneNumber()
            .value()
        );
    }

    public abstract Builder deviceId(String deviceId);

    abstract Builder signedData(String signedData);

    public final Builder signedData(byte[] signedData) {
      return this.signedData(Base64.encodeToString(signedData, Base64.NO_WRAP));
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

    public abstract RetrofitApiOpenSessionBody build();
  }
}
