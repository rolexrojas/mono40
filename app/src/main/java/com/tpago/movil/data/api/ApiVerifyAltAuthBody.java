package com.tpago.movil.data.api;

import android.util.Base64;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.domain.user.User;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ApiVerifyAltAuthBody {

  public static Builder builder() {
    return new AutoValue_ApiVerifyAltAuthBody.Builder();
  }

  ApiVerifyAltAuthBody() {
  }

  @SerializedName("account-id")
  public abstract int userId();

  @SerializedName("msisdn")
  public abstract String userPhoneNumber();

  @SerializedName("imei")
  public abstract String deviceId();

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
      return this.userId(user.id())
        .userPhoneNumber(
          user.phoneNumber()
            .value()
        );
    }

    public abstract Builder deviceId(String deviceId);

    abstract Builder signedData(String signedData);

    public final Builder signedData(byte[] signedData) {
      return this.signedData(
        Base64.encodeToString(
          signedData,
          Base64.DEFAULT
        )
      );
    }

    public abstract ApiVerifyAltAuthBody build();
  }
}
