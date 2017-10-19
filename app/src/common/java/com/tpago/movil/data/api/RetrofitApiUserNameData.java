package com.tpago.movil.data.api;

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
public abstract class RetrofitApiUserNameData {

  public static TypeAdapter<RetrofitApiUserNameData> typeAdapter(Gson gson) {
    return new AutoValue_RetrofitApiUserNameData.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_RetrofitApiUserNameData.Builder();
  }

  public static RetrofitApiUserNameData create(User user) {
    ObjectHelper.checkNotNull(user, "user");
    return builder()
      .firstName(user.firstName())
      .lastName(user.lastName())
      .build();
  }

  RetrofitApiUserNameData() {
  }

  @SerializedName("name")
  public abstract String firstName();

  @SerializedName("last-name")
  public abstract String lastName();

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

    public abstract Builder firstName(String firstName);

    public abstract Builder lastName(String lastName);

    public abstract RetrofitApiUserNameData build();
  }
}
