package com.tpago.movil;

import com.tpago.movil.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
public final class Digits {
  public static String stringify(List<Digit> digits) {
    Preconditions.assertNotNull(digits, "digits == null");
    final StringBuilder builder = new StringBuilder();
    for (Digit digit : digits) {
      builder.append(digit.getValue());
    }
    return builder.toString();
  }

  public static List<Digit> getDigits(PhoneNumber phoneNumber) {
    Preconditions.assertNotNull(phoneNumber, "phoneNumber == null");
    final String phoneNumberValue = phoneNumber.getValue();
    final List<Digit> list = new ArrayList<>();
    for (int i = 0; i < phoneNumberValue.length(); i++) {
      list.add(Digit.find(Integer.parseInt(String.valueOf(phoneNumberValue.charAt(i)))));
    }
    return list;
  }

  private Digits() {
    throw new AssertionError("Cannot be instantiated");
  }
}
