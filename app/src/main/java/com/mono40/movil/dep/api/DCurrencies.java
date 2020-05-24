package com.mono40.movil.dep.api;

/**
 * @author hecvasro
 */
@Deprecated
public final class DCurrencies {

  private static final String API = "DOP";
  private static final String APP = "RD$";

  public static String map(String currency) {
    if (currency.equals(API)) {
      return APP;
    } else {
      return currency;
    }
  }

  private DCurrencies() {
    throw new AssertionError("Cannot be instantiated");
  }
}
