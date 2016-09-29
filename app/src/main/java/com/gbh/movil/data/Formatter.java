package com.gbh.movil.data;

import android.support.annotation.NonNull;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class Formatter {
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
}
