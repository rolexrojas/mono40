package com.mono40.movil.dep;

import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
public final class Preconditions {

  public static <T> T assertNotNull(T reference, String message) {
    if (ObjectHelper.isNull(reference)) {
      throw new NullPointerException(message);
    }
    return reference;
  }

  private Preconditions() {
    throw new AssertionError("Cannot be instantiated");
  }
}
