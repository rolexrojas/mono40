package com.tpago.movil.dep.api;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author Hector Vasquez
 */
@Deprecated
@AutoValue
public abstract class UserData {

  private static final String DEFAULT_FIRST_NAME = "Usuario";
  private static final String DEFAULT_LAST_NAME = "tPago";

  public static TypeAdapter<UserData> typeAdapter(Gson gson) {
    return new AutoValue_UserData.GsonTypeAdapter(gson)
      .setDefaultFirstName(DEFAULT_FIRST_NAME)
      .setDefaultLastName(DEFAULT_LAST_NAME);
  }

  public static Builder createBuilder() {
    return new AutoValue_UserData.Builder()
      .firstName(DEFAULT_FIRST_NAME)
      .lastName(DEFAULT_LAST_NAME);
  }

  @SerializedName("id")
  public abstract int id();

  @SerializedName("msisdn")
  public abstract String phoneNumber();

  @SerializedName("email")
  public abstract String email();

  @SerializedName("updateName")
  public abstract String firstName();

  @SerializedName("last-updateName")
  public abstract String lastName();

  @SerializedName("profilePicUrl")
  @Nullable
  public abstract String pictureUri();

  @AutoValue.Builder
  public static abstract class Builder {

    public abstract Builder id(int id);

    public abstract Builder phoneNumber(String phoneNumber);

    public abstract Builder email(String email);

    public abstract Builder firstName(String firstName);

    public abstract Builder lastName(String lastName);

    public abstract Builder pictureUri(String pictureUri);

    public abstract UserData build();
  }
}
