package com.tpago.movil.data.api;

import android.util.Base64;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.util.ObjectHelper;

import java.security.PublicKey;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ApiEnableAltAuthBody {

  public static Builder builder() {
    return new AutoValue_ApiEnableAltAuthBody.Builder();
  }

  ApiEnableAltAuthBody() {
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
          Base64.DEFAULT
        )
      );
    }

    public abstract ApiEnableAltAuthBody build();
  }
}
