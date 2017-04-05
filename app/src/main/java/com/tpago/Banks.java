package com.tpago;

import android.graphics.Color;

import com.tpago.movil.domain.Bank;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author hecvasro
 */
public final class Banks {
  private static final int BANK_ADEMI = 38;
  private static final int BANK_ADOPEM = 44;
  private static final int BANK_ALAVER = 35;
  private static final int BANK_BDI = 36;
  private static final int BANK_LOPEZ_DE_HARO = 37;
  private static final int BANK_POPULAR = 5;
  private static final int BANK_PROGRESO = 24;
  private static final int BANK_RESERVAS = 4;
  private static final int BANK_UNION = 45;

  /**
   * Temporary fix for: <a href="https://trello.com/c/09CG3iLy">https://trello.com/c/09CG3iLy</a>
   */
  @Deprecated
  public static String getName(Bank bank) {
    switch (bank.getCode()) {
      case BANK_POPULAR:
        return "Popular";
      default:
        return bank.getName();
    }
  }

  /**
   * Temporary fix for: TODO
   */
  @Deprecated
  public static int getColor(Bank bank) {
    switch (bank.getCode()) {
      case BANK_ADEMI:
        return Color.parseColor("#008286");
      case BANK_ADOPEM:
        return Color.parseColor("#DF4F2A");
      case BANK_ALAVER:
        return Color.parseColor("#1D5898");
      case BANK_BDI:
        return Color.parseColor("#515251");
      case BANK_LOPEZ_DE_HARO:
        return Color.parseColor("#26335D");
      case BANK_POPULAR:
        return Color.parseColor("#004990");
      case BANK_PROGRESO:
        return Color.parseColor("#0097D7");
      case BANK_RESERVAS:
        return Color.parseColor("#294661");
      case BANK_UNION:
        return Color.parseColor("#FD4F57");
      default:
        return Color.parseColor("#D8D8D8");
    }
  }

  public static void sort(List<Bank> bankList) {
    Collections.sort(
      bankList,
      new Comparator<Bank>() {
        @Override
        public int compare(Bank ba, Bank bb) {
          return getName(ba).compareTo(getName(bb));
        }
      });
  }

  private Banks() {
    throw new AssertionError("Cannot be instantiated");
  }
}
