package com.tpago.movil.api;

/**
 * @author hecvasro
 */
public final class Currencies {
  private static final String DOP_API = "DOP";
  private static final String DOP_APP = "RD$";

  public static final String map(String currency) {
    if (currency.equals(DOP_APP)) {
      return DOP_API;
    } else {
      return DOP_APP;
    }
  }

  private Currencies() {
    throw new AssertionError("Cannot be instantiated");
  }
}
