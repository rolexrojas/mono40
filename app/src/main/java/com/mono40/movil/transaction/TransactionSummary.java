package com.mono40.movil.transaction;

import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class TransactionSummary implements Parcelable {

  public static TypeAdapter<TransactionSummary> typeAdapter(Gson gson) {
    return new AutoValue_TransactionSummary.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_TransactionSummary.Builder();
  }

  TransactionSummary() {
  }

  @SerializedName("transaction-id")
  public abstract String id();

  @SerializedName("transaction-message")
  @Nullable
  public abstract String message();

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder id(String id);

    public abstract Builder message(@Nullable String message);

    public abstract TransactionSummary build();
  }
}
