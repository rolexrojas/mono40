package com.mono40.movil.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.company.bank.Bank;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class RetrofitApiPreRegistrationBody {

  public static TypeAdapter<RetrofitApiPreRegistrationBody> typeAdapter(Gson gson) {
    return new AutoValue_RetrofitApiPreRegistrationBody.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_RetrofitApiPreRegistrationBody.Builder();
  }

  RetrofitApiPreRegistrationBody() {
  }

  @SerializedName("msisdn")
  abstract String phoneNumber();

  @SerializedName("email")
  abstract String email();

  @SerializedName("name")
  abstract String firstName();

  @SerializedName("lastName")
  abstract String lastName();

  @SerializedName("bank")
  abstract Bank bank();

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

    public abstract Builder phoneNumber(String phoneNumber);

    public abstract Builder email(String email);

    public abstract Builder firstName(String firstName);

    public abstract Builder lastName(String lastName);

    public abstract Builder bank(Bank bank);

    public abstract RetrofitApiPreRegistrationBody build();
  }
}
