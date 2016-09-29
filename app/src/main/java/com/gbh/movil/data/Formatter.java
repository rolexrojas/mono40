package com.gbh.movil.data;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class Formatter {
  /**
   * TODO
   */
  private static final DateFormat FORMAT_DATE
    = new SimpleDateFormat("YYYY MMMM dd", Locale.getDefault());

  private Formatter() {
  }

  /**
   * TODO
   *
   * @param currencyCode
   *   TODO
   * @param value
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static String currency(@NonNull String currencyCode, double value) {
    final Currency currency = Currency.getInstance(currencyCode);
    Timber.d("Currency code = %1$s", currency.getCurrencyCode());
    Timber.d("Currency symbol = %1$s", currency.getSymbol());
    final NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
    format.setCurrency(currency);
    format.setMinimumFractionDigits(2);
    format.setMaximumFractionDigits(2);
    Timber.d("Format currency = %1$s", format.getCurrency());
    Timber.d("Format minimum fraction digits = %1$s", format.getMinimumFractionDigits());
    Timber.d("Format maximum fraction digits = %1$s", format.getMaximumFractionDigits());
    Timber.d("Format rounding mode = %1$s", format.getRoundingMode());
    return format.format(value);
  }

  /**
   * TODO
   *
   * @param date
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static String date(@NonNull Date date) {
    return FORMAT_DATE.format(date);
  }
}
