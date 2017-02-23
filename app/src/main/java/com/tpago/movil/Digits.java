package com.tpago.movil;

import com.tpago.movil.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
public final class Digits {
  public static String stringify(List<Digit> digits) {
    Preconditions.checkNotNull(digits, "digits == null");
    final StringBuilder builder = new StringBuilder();
    for (Digit digit : digits) {
      builder.append(digit.getValue());
    }
    return PhoneNumber.format(builder.toString());
  }

  public static List<Digit> getDigits(PhoneNumber phoneNumber) {
    Preconditions.checkNotNull(phoneNumber, "phoneNumber == null");
    final String[] array = phoneNumber.getValue().split("");
    final List<Digit> list = new ArrayList<>();
    for (String s : array) {
      list.add(Digit.find(Integer.parseInt(s)));
    }
    return list;
  }

  private Digits() {
    throw new AssertionError("Cannot be instantiated");
  }
}
