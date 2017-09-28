package com.tpago.movil.d.domain;

import java.util.regex.Pattern;

import static com.tpago.movil.text.Texts.checkIfNotEmpty;

/**
 * @author hecvasro
 */
@Deprecated
public final class Password {
  private static final Pattern PATTERN = Pattern.compile("\\A[A-Za-z0-9]{8,}\\z");

  public static boolean checkIfValid(String password) {
    return checkIfNotEmpty(password) && PATTERN.matcher(password).matches();
  }

  private Password() {
    throw new AssertionError("Cannot be instantiated");
  }
}
