package com.tpago.movil;

import android.graphics.Color;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Bank implements Serializable, Parcelable {
  private static final int BANK_ADEMI = 38;
  private static final int BANK_ADOPEM = 44;
  private static final int BANK_ALAVER = 35;
  private static final int BANK_BDI = 36;
  private static final int BANK_LOPEZ_DE_HARO = 37;
  private static final int BANK_POPULAR = 5;
  private static final int BANK_PROGRESO = 24;
  private static final int BANK_RESERVAS = 4;
  private static final int BANK_UNION = 45;

  public static Bank create(int code, String id, String name, String logoUri) {
    return new AutoValue_Bank(code, id, name, logoUri);
  }

  /**
   * Temporary fix for: <a href="https://trello.com/c/09CG3iLy">https://trello.com/c/09CG3iLy</a>
   */
  public static String getName(Bank bank) {
    switch (bank.getCode()) {
      case BANK_POPULAR: return "Popular";
      default: return bank.getName();
    }
  }

  /**
   * Temporary fix for: TODO
   */
  public static int getColor(Bank bank) {
    switch (bank.getCode()) {
      case BANK_ADEMI: return Color.parseColor("#008286");
      case BANK_ADOPEM: return Color.parseColor("#DF4F2A");
      case BANK_ALAVER: return Color.parseColor("#1D5898");
      case BANK_BDI: return Color.parseColor("#515251");
      case BANK_LOPEZ_DE_HARO: return Color.parseColor("#26335D");
      case BANK_POPULAR: return Color.parseColor("#004990");
      case BANK_PROGRESO: return Color.parseColor("#0097D7");
      case BANK_RESERVAS: return Color.parseColor("#294661");
      case BANK_UNION: return Color.parseColor("#FD4F57");
      default: return Color.parseColor("#D8D8D8");
    }
  }

  public static TypeAdapter<Bank> typeAdapter(Gson gson) {
    return new AutoValue_Bank.GsonTypeAdapter(gson);
  }

  @SerializedName("bank-code") public abstract int getCode();
  @SerializedName("bank-id") public abstract String getId();
  @SerializedName("bank-name") public abstract String getName();
  @SerializedName("bank-logo-uri") public abstract String getImageUriTemplate();
}
