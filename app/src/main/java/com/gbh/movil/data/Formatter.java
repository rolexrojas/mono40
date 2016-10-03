package com.gbh.movil.data;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class Formatter {
  /**
   * TODO
   */
  private static final Locale LOCALE_EN = new Locale("en");

  /**
   * TODO
   */
  private static final Locale LOCALE_ES = new Locale("es");

  /**
   * TODO
   */
  private static final DateFormat FORMAT_DATE = new SimpleDateFormat("yyyy MMMM dd", LOCALE_ES);

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
    return String.format(LOCALE_EN, "%1$s%2$s$%3$,.2f", value < 0 ? "-" : "", currencyCode,
      Math.abs(value));
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
