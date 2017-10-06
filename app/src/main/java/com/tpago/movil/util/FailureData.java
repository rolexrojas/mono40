package com.tpago.movil.util;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Failure value representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class FailureData {

  public static Builder builder() {
    return new AutoValue_FailureData.Builder();
  }

  FailureData() {
  }

  @Code
  public abstract int code();

  @Nullable
  public abstract String description();

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  @IntDef({
    Code.ALREADY_ASSOCIATED_DEVICE,
    Code.ALREADY_ASSOCIATED_PROFILE,
    Code.ALREADY_REGISTERED_EMAIL,
    Code.ALREADY_REGISTERED_USERNAME,
    Code.INCORRECT_USERNAME_AND_PASSWORD,
    Code.INVALID_DEVICE_ID,
    Code.INVALID_EMAIL,
    Code.INVALID_PASSWORD,
    Code.INVALID_PHONE_NUMBER,
    Code.INVALID_PIN,
    Code.UNASSOCIATED_PHONE_NUMBER,
    Code.UNASSOCIATED_PROFILE,
    Code.UNAVAILABLE_SERVICE,
    Code.INCORRECT_CODE
  })
  @Retention(RetentionPolicy.SOURCE)
  public @interface Code {

    int ALREADY_ASSOCIATED_DEVICE = 112;
    int ALREADY_ASSOCIATED_PROFILE = 12;
    int ALREADY_REGISTERED_EMAIL = 55;
    int ALREADY_REGISTERED_USERNAME = 21;
    int INCORRECT_USERNAME_AND_PASSWORD = 4;
    int INVALID_DEVICE_ID = 9;
    int INVALID_EMAIL = 0;
    int INVALID_PASSWORD = 1618;
    int INVALID_PHONE_NUMBER = 13;
    int INVALID_PIN = 16;
    int UNASSOCIATED_PHONE_NUMBER = 216;
    int UNASSOCIATED_PROFILE = 144;
    int UNAVAILABLE_SERVICE = 14;

    int INCORRECT_CODE = 7000;
  }

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder code(@Code int code);

    public abstract Builder description(@Nullable String description);

    public abstract FailureData build();
  }
}
