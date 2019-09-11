package com.tpago.movil.api;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.Name;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.session.User;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class ApiBeneficiary {

  public static TypeAdapter<ApiBeneficiary> typeAdapter(Gson gson) {
    return new AutoValue_ApiBeneficiary.GsonTypeAdapter(gson);
  }

  private static Builder builder() {
    return new AutoValue_ApiBeneficiary.Builder();
  }

  private static Builder builder(User user) {
    ObjectHelper.checkNotNull(user, "user");
    final String entityDetail = user.phoneNumber()
      .value();
    final Partner carrier = user.carrier();
    final Integer entityId = ObjectHelper.isNotNull(carrier) ? carrier.code() : null;
    final String name = user.name()
      .toString();
    return builder()
      .entityDetail(entityDetail)
      .entityId(entityId)
      .isEntityPhoneNumber(true)
      .name(name);
  }

  public static ApiBeneficiary create(User user, Name name) {
    ObjectHelper.checkNotNull(name, "name");
    return builder(user)
      .name(name.toString())
      .build();
  }

  public static ApiBeneficiary create(User user, Partner carrier) {
    ObjectHelper.checkNotNull(carrier, "carrier");
    return builder(user)
      .entityId(carrier.code())
      .build();
  }

  ApiBeneficiary() {
  }

  @SerializedName("entityDetail")
  public abstract String entityDetail();

  @SerializedName("entity")
  @Nullable
  public abstract Integer entityId();

  @SerializedName("phone-number")
  public abstract boolean isEntityPhoneNumber();

  @SerializedName("name")
  public abstract String name();

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

    public abstract Builder entityDetail(String entityDetail);

    public abstract Builder entityId(@Nullable Integer entityId);

    public abstract Builder isEntityPhoneNumber(boolean isEntityPhoneNumber);

    public abstract Builder name(String name);

    public abstract ApiBeneficiary build();
  }
}
