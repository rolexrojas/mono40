package com.tpago.movil.data.api;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.payment.Carrier;
import com.tpago.movil.session.User;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class RetrofitApiBeneficiaryData {

  public static TypeAdapter<RetrofitApiBeneficiaryData> typeAdapter(Gson gson) {
    return new AutoValue_RetrofitApiBeneficiaryData.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_RetrofitApiBeneficiaryData.Builder();
  }

  public static RetrofitApiBeneficiaryData create(User user) {
    ObjectHelper.checkNotNull(user, "user");
    final String entityDetail = user.phoneNumber()
      .value();
    final Carrier carrier = user.carrier();
    final Integer entityId = ObjectHelper.isNotNull(carrier) ? carrier.code() : null;
    return builder()
      .entityDetail(entityDetail)
      .entityId(entityId)
      .isEntityPhoneNumber(true)
      .name(user.name())
      .build();
  }

  RetrofitApiBeneficiaryData() {
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

    public abstract RetrofitApiBeneficiaryData build();
  }
}
