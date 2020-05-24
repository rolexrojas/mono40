package com.mono40.movil.product.disbursable;

import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.Currency;
import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.util.ComparisonChain;
import com.mono40.movil.util.digit.DigitUtil;
import com.mono40.movil.util.IncludeHashEquals;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Disbursable implements Comparable<Disbursable>, Parcelable {

  public static TypeAdapter<Disbursable> typeAdapter(Gson gson) {
    return new AutoValue_Disbursable.GsonTypeAdapter(gson);
  }

  Disbursable() {
  }

  @IncludeHashEquals
  @SerializedName("transaction-type")
  @Bank.Transaction.Type
  public abstract String type();

  @IncludeHashEquals
  @SerializedName("salary-advance-id")
  public abstract int id();

  @SerializedName("client-type")
  public abstract String clientIdType();

  @SerializedName("client-id")
  public abstract String clientId();

  @SerializedName("account-number-from")
  public abstract String number();

  @Memoized
  public String numberSanitized() {
    return DigitUtil.removeNonDigits(this.number());
  }

  @SerializedName("alias-from")
  public abstract String alias();

  @SerializedName("destination-account-type")
  public abstract String destinationProductType();

  @SerializedName("account-number-to")
  public abstract String destinationProductNumber();

  @Memoized
  public String destinationProductNumberSanitized() {
    return DigitUtil.removeNonDigits(this.destinationProductNumber());
  }

  @SerializedName("alias-to")
  public abstract String destinationProductAlias();

  public abstract Currency currency();

  @SerializedName("available-amount")
  public abstract BigDecimal balance();

  @SerializedName("min-amount")
  public abstract BigDecimal minimumAmount();

  @SerializedName("max-amount")
  public abstract BigDecimal maximumAmount();

  @Override
  public int compareTo(@NonNull Disbursable that) {
    return ComparisonChain.create()
      .compare(this.type(), that.type())
      .compare(this.id(), that.id())
      .result();
  }
}
