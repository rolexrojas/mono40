package com.tpago.movil;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.tpago.movil.domain.Bank;

import java.math.BigDecimal;

/**
 * @author hecvasro
 */
public abstract class Product implements Parcelable {
  @SerializedName("bank") public abstract Bank getBank();
  @SerializedName("account-type") public abstract Type getType();
  @SerializedName("account-alias") public abstract String getAlias();
  @SerializedName("account-number") public abstract String getNumber();
  @SerializedName("currency") public abstract String getCurrency();
  @SerializedName("query-fee") public abstract BigDecimal getQueryFee();

  public enum Type {
    LOAN, // PRESTAMO
    PPA, // CUENTA PREPAGO  (Los monederos tienen este tipo de cuenta)
    SAVELLA, // BANCO UNION  AHORRO (CREDITO Y DEBITO)
    TBD, // BANCO UNION TARJETA DE DEBITO VISA
    SAV, // SAVING CUENTA DE AHORRO
    CDA, // CITIBANK CUENTA  QUE SOLO PERMITE CREDITO (Pagos suplidores citi)
    DDA, // CUENTA CORRIENTE (DEBIT DIRECT ACCOUNT)
    AMEX, // TARJETA AMEX
    CC, // TARJETA VISA/MASTERCARD
    SAVCLARO // BANCO UNION AHORRO (SOLO PERMITE CREDITOS)
  }
}
