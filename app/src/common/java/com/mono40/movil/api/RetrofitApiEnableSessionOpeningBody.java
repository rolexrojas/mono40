package com.mono40.movil.api;

import android.util.Base64;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.util.ObjectHelper;

import java.security.PublicKey;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class RetrofitApiEnableSessionOpeningBody {

  public static TypeAdapter<RetrofitApiEnableSessionOpeningBody> typeAdapter(Gson gson) {
    return new AutoValue_RetrofitApiEnableSessionOpeningBody.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_RetrofitApiEnableSessionOpeningBody.Builder();
  }

  RetrofitApiEnableSessionOpeningBody() {
  }

  @SerializedName("public-key")
  public abstract String publicKey();

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

    abstract Builder publicKey(String publicKey);

    public final Builder publicKey(PublicKey publicKey) {
      return this.publicKey(
        Base64.encodeToString(
          ObjectHelper.checkNotNull(publicKey, "publicKey")
            .getEncoded(),
          Base64.NO_WRAP
        )
      );
    }

    public abstract RetrofitApiEnableSessionOpeningBody build();
  }
}
