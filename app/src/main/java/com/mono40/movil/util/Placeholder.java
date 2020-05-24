package com.mono40.movil.util;

/**
 * @author hecvasro
 */
public final class Placeholder {

  private static final Placeholder INSTANCE = new Placeholder();

  public static Placeholder get() {
    return INSTANCE;
  }

  private Placeholder() {
  }
}
