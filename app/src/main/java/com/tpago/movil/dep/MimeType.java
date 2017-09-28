package com.tpago.movil.dep;

/**
 * @author hecvasro
 */
@Deprecated
public final class MimeType {
  public static final String IMAGE = "image/*";

  private MimeType() {
    throw new AssertionError("Cannot be instantiated");
  }
}
