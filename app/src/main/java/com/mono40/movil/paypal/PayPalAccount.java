package com.mono40.movil.paypal;

import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.util.ComparisonChain;

/**
 * Represents a PayPal account
 */
@AutoValue
public abstract class PayPalAccount implements Parcelable, Comparable<PayPalAccount> {

  public static TypeAdapter<PayPalAccount> typeAdapter(Gson gson) {
    return new AutoValue_PayPalAccount.GsonTypeAdapter(gson);
  }

  PayPalAccount() {
  }

  @SerializedName("paypalEmail")
  public abstract String email();

  @SerializedName("alias")
  public abstract String alias();

  @SerializedName("msisdn")
  abstract String msisdn();

  @SerializedName("documentId")
  abstract String documentId();

  @SerializedName("id")
  abstract int id();

  @SerializedName("paypalAccountId")
  abstract String accountId();

  @SerializedName("preApprovalKey")
  abstract String preApprovalKey();

  @SerializedName("token")
  abstract String token();

  @Override
  public int compareTo(@NonNull PayPalAccount that) {
    return ComparisonChain.create()
      .compare(this.email(), that.email())
      .compare(this.alias(), that.alias())
      .result();
  }
}
