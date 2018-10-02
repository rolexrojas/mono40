package com.tpago.movil.product.disbursable;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.R;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.util.ComparisonChain;
import com.tpago.movil.util.ObjectHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class DisbursableProduct implements Comparable<DisbursableProduct>, Parcelable {

  public static DisbursableProduct create(Bank bank, Disbursable disbursable) {
    return new AutoValue_DisbursableProduct(bank, disbursable);
  }

  private TermData termData;
  private FeeData feeData;

  DisbursableProduct() {
  }

  public abstract Bank bank();

  public abstract Disbursable disbursable();

  @Memoized
  @Bank.Transaction.Type
  public String type() {
    return this.disbursable()
      .type();
  }

  @Nullable
  public final TermData termData() {
    return this.termData;
  }

  public final void termData(TermData data) {
    this.termData = ObjectHelper.checkNotNull(data, "termData");
  }

  @Nullable
  public final FeeData feeData() {
    return this.feeData;
  }

  public final void feeData(FeeData data) {
    this.feeData = ObjectHelper.checkNotNull(data, "feeData");
  }

  @Override
  public int compareTo(@NonNull DisbursableProduct that) {
    return ComparisonChain.create()
      .compare(this.bank(), that.bank())
      .compare(this.disbursable(), that.disbursable())
      .result();
  }

  @AutoValue
  public static abstract class TermData {

    public static TypeAdapter<TermData> typeAdapter(Gson gson) {
      return new AutoValue_DisbursableProduct_TermData.GsonTypeAdapter(gson);
    }

    TermData() {
    }

    public abstract BigDecimal amount();

    @SerializedName("min-quota")
    public abstract int minimumTerm();

    @SerializedName("max-quota")
    public abstract int maximumTerm();
  }

  @AutoValue
  public static abstract class FeeData {

    private static final Map<String, Integer> RATE_TYPE_STRING_IDS;

    static {
      RATE_TYPE_STRING_IDS = new HashMap<>();
      RATE_TYPE_STRING_IDS.put(RateType.DAILY, R.string.daily);
      RATE_TYPE_STRING_IDS.put(RateType.WEEKLY, R.string.weekly);
      RATE_TYPE_STRING_IDS.put(RateType.MONTHLY, R.string.monthly);
      RATE_TYPE_STRING_IDS.put(RateType.ANNUAL, R.string.annual);
    }

    public static TypeAdapter<FeeData> typeAdapter(Gson gson) {
      return new AutoValue_DisbursableProduct_FeeData.GsonTypeAdapter(gson);
    }

    FeeData() {
    }

    @SerializedName("transaction-amount")
    public abstract BigDecimal transactionAmount();

    @SerializedName("quota-amount")
    public abstract int term();

    @SerializedName("rate-type")
    public abstract String rateType();

    public final String rateType(StringMapper stringMapper) {
      ObjectHelper.checkNotNull(stringMapper, "stringMapper");
      final String rateType = this.rateType();
      if (!RATE_TYPE_STRING_IDS.containsKey(rateType)) {
        return rateType;
      }
      return stringMapper.apply(RATE_TYPE_STRING_IDS.get(rateType));
    }

    public abstract BigDecimal rate();

    public abstract BigDecimal insurance();

    @SerializedName("new-balance")
    public abstract BigDecimal newBalance();

    @SerializedName("new-quota")
    public abstract BigDecimal fee();

    @StringDef({
      RateType.DAILY,
      RateType.MONTHLY,
      RateType.ANNUAL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface RateType {

      String DAILY = "DAILY";
      String WEEKLY = "WEEKLY";
      String MONTHLY = "MONTHLY";
      String ANNUAL = "ANNUAL";
    }
  }
}
