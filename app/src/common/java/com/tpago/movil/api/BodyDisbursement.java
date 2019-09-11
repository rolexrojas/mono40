package com.tpago.movil.api;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.Currency;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.product.disbursable.Disbursable;
import com.tpago.movil.product.disbursable.DisbursableProduct;
import com.tpago.movil.util.IncludeHashEquals;
import com.tpago.movil.util.ObjectHelper;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class  BodyDisbursement {

  public static TypeAdapter<BodyDisbursement> typeAdapter(Gson gson) {
    return new AutoValue_BodyDisbursement.GsonTypeAdapter(gson);
  }

  public static Builder builder(DisbursableProduct product) {
    final Builder builder = new AutoValue_BodyDisbursement.Builder();
    if (ObjectHelper.isNotNull(product)) {
      final Disbursable disbursable = product.disbursable();
      builder
        .bank(product.bank())
        .type(disbursable.type())
        .id(disbursable.id())
        .clientIdType(disbursable.clientIdType())
        .clientId(disbursable.clientId())
        .number(disbursable.number())
        .alias(disbursable.alias())
        .destinationProductType(disbursable.destinationProductType())
        .destinationProductNumber(disbursable.destinationProductNumber())
        .destinationProductAlias(disbursable.destinationProductAlias())
        .currency(disbursable.currency())
        .balance(disbursable.balance())
        .minimumAmount(disbursable.minimumAmount())
        .maximumAmount(disbursable.maximumAmount());
      final DisbursableProduct.TermData termData = product.termData();
      if (ObjectHelper.isNotNull(termData)) {
        builder
          .amount(termData.amount())
          .minimumTerm(termData.minimumTerm())
          .maximumTerm(termData.maximumTerm());
      }
      final DisbursableProduct.FeeData feeData = product.feeData();
      if (ObjectHelper.isNotNull(feeData)) {
        builder
          .transactionAmount(feeData.transactionAmount())
          .term(feeData.term())
          .rateType(feeData.rateType())
          .rate(feeData.rate())
          .insurance(feeData.insurance())
          .newBalance(feeData.newBalance())
          .fee(feeData.fee());
      }
    }
    return builder;
  }

  public static Builder builder() {
    return builder(null);
  }

  BodyDisbursement() {
  }

  public abstract Bank bank();

  @SerializedName("transaction-type")
  @IncludeHashEquals
  @Nullable
  @Bank.Transaction.Type
  public abstract String type();

  @IncludeHashEquals
  @SerializedName("salary-advance-id")
  @Nullable
  public abstract Integer id();

  @SerializedName("client-type")
  @Nullable
  public abstract String clientIdType();

  @SerializedName("client-id")
  @Nullable
  public abstract String clientId();

  @SerializedName("account-number-from")
  @Nullable
  public abstract String number();

  @SerializedName("alias-from")
  @Nullable
  public abstract String alias();

  @SerializedName("destination-account-type")
  @Nullable
  public abstract String destinationProductType();

  @SerializedName("account-number-to")
  @Nullable
  public abstract String destinationProductNumber();

  @SerializedName("alias-to")
  @Nullable
  public abstract String destinationProductAlias();

  @Nullable
  public abstract Currency currency();

  @SerializedName("available-amount")
  @Nullable
  public abstract BigDecimal balance();

  @SerializedName("min-amount")
  @Nullable
  public abstract BigDecimal minimumAmount();

  @SerializedName("max-amount")
  @Nullable
  public abstract BigDecimal maximumAmount();

  @Nullable
  public abstract BigDecimal amount();

  @SerializedName("transaction-amount")
  @Nullable
  public abstract BigDecimal transactionAmount();

  @SerializedName("min-quota")
  @Nullable
  public abstract Integer minimumTerm();

  @SerializedName("max-quota")
  @Nullable
  public abstract Integer maximumTerm();

  @SerializedName("quota-amount")
  @Nullable
  public abstract Integer term();

  @SerializedName("rate-type")
  @Nullable
  public abstract String rateType();

  @Nullable
  public abstract BigDecimal rate();

  @Nullable
  public abstract BigDecimal insurance();

  @SerializedName("new-balance")
  @Nullable
  public abstract BigDecimal newBalance();

  @SerializedName("new-quota")
  @Nullable
  public abstract BigDecimal fee();

  @Nullable
  public abstract String pin();

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder bank(Bank bank);

    public abstract Builder type(@Nullable String type);

    public abstract Builder id(@Nullable Integer id);

    public abstract Builder clientIdType(@Nullable String clientIdType);

    public abstract Builder clientId(@Nullable String clientType);

    public abstract Builder number(@Nullable String number);

    public abstract Builder alias(@Nullable String alias);

    public abstract Builder destinationProductType(@Nullable String destinationProductType);

    public abstract Builder destinationProductNumber(@Nullable String destinationProductNumber);

    public abstract Builder destinationProductAlias(@Nullable String destinationProductAlias);

    public abstract Builder currency(@Nullable Currency currency);

    public abstract Builder balance(@Nullable BigDecimal balance);

    public abstract Builder minimumAmount(@Nullable BigDecimal minimumAmount);

    public abstract Builder maximumAmount(@Nullable BigDecimal maximumAmount);

    public abstract Builder amount(@Nullable BigDecimal amount);

    public abstract Builder transactionAmount(@Nullable BigDecimal transactionAmount);

    public abstract Builder minimumTerm(@Nullable Integer minimumTerm);

    public abstract Builder maximumTerm(@Nullable Integer maximumTerm);

    public abstract Builder term(@Nullable Integer term);

    public abstract Builder rateType(@Nullable String rateType);

    public abstract Builder rate(@Nullable BigDecimal rate);

    public abstract Builder insurance(@Nullable BigDecimal insurance);

    public abstract Builder newBalance(@Nullable BigDecimal newBalance);

    public abstract Builder fee(@Nullable BigDecimal fee);

    public abstract Builder pin(@Nullable String pin);

    public abstract BodyDisbursement build();
  }
}
