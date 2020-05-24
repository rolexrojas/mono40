package com.mono40.movil.util;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * @author hecvasro
 */
public final class Rate {

  public static String format(BigDecimal value) {
    final BigDecimal v = ObjectHelper.checkNotNull(value, "value")
      .multiply(BigDecimal.valueOf(100));
    return String.format(Locale.ENGLISH, "%1$.2f", v);
  }

  private Rate() {
  }
}
