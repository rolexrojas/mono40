package com.tpago.movil.product;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.tpago.movil.Currency;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.util.ComparisonChain;
import com.tpago.movil.util.digit.DigitUtil;
import com.tpago.movil.util.IncludeHashEquals;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;

/**
 * Product representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class Product implements Comparable<Product> {

  public static TypeAdapter<Product> typeAdapter(Gson gson) {
    return new AutoValue_Product.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_Product.Builder();
  }

  Product() {
  }

  @IncludeHashEquals
  public abstract Bank bank();

  @SerializedName("account-type")
  @IncludeHashEquals
  @Type
  public abstract String type();

  @SerializedName("account-number")
  @IncludeHashEquals
  public abstract String number();

  @Memoized
  public String numberSanitized() {
    String number = DigitUtil.removeNonDigits(this.number());
    return "•••• •••• •••• " + number.substring(number.length() - 4);
  }

  @SerializedName("account-alias")
  public abstract String alias();

  public abstract Currency currency();

  /**
   * Cost of querying its balance.
   */
  @SerializedName("query-fee")
  public abstract BigDecimal balanceQueryCost();

  @SerializedName("image-url")
  @Nullable
  public abstract String imageTemplate();

  /**
   * Indicates whether it's a payment method or not.
   */
  @SerializedName("payable")
  public abstract boolean isPaymentMethod();

  /**
   * Indicates whether it's the primary payment method or not.
   */
  @SerializedName("default-account")
  public abstract boolean isPrimaryPaymentMethod();

  /**
   * Id for the cards
   */
  @SerializedName("altpan-key")
  @Nullable
  public abstract String altpanKey();

  @Override
  public int compareTo(@NonNull Product that) {
    return ComparisonChain.create()
      .compare(this.bank(), that.bank())
      .compare(this.type(), that.type())
      .compare(this.number(), that.number())
      .compare(this.alias(), that.alias())
      .result();
  }

  @Retention(RetentionPolicy.SOURCE)
  @StringDef({
    Type.AMEX,
    Type.CC,
    Type.CDA,
    Type.DDA,
    Type.LOAN,
    Type.PPA,
    Type.SAV,
    Type.SAVCLARO,
    Type.SAVELLA,
    Type.TBD
  })
  public @interface Type {

    String AMEX = "AMEX"; // TARJETA AMEX
    String CC = "CC"; // TARJETA VISA/MASTERCARD
    String CDA = "CDA"; // CITIBANK CUENTA  QUE SOLO PERMITE CREDITO (Pagos suplidores citi)
    String DDA = "DDA"; // CUENTA CORRIENTE (DEBIT DIRECT ACCOUNT)
    String LOAN = "LOAN"; // PRESTAMO
    String PPA = "PPA"; // CUENTA PREPAGO  (Los monederos tienen este tipo de cuenta)
    String SAV = "SAV";// SAVING CUENTA DE AHORRO
    String SAVCLARO = "SAVCLARO"; // BANCO UNION AHORRO (SOLO PERMITE CREDITOS)
    String SAVELLA = "SAVELLA"; // BANCO UNION  AHORRO (CREDITO Y DEBITO)
    String TBD = "TBD"; // BANCO UNION TARJETA DE DEBITO VISA
  }

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder bank(Bank bank);

    public abstract Builder type(@Type String type);

    public abstract Builder number(String number);

    public abstract Builder alias(String alias);

    public abstract Builder currency(Currency currency);

    public abstract Builder balanceQueryCost(BigDecimal balanceQueryCost);

    public abstract Builder imageTemplate(@Nullable String imageTemplate);

    public abstract Builder isPaymentMethod(boolean isPaymentMethod);

    public abstract Builder isPrimaryPaymentMethod(boolean isPrimaryPaymentMethod);

    public abstract Builder altpanKey(String altpanKey);

    public abstract Product build();
  }
}
