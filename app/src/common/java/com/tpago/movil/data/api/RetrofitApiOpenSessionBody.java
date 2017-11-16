package com.tpago.movil.data.api;

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

    public abstract RetrofitApiOpenSessionBody build();
  }
}
