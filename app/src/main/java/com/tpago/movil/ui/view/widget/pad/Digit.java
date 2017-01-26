package com.tpago.movil.ui.view.widget.pad;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link NumPad Num pad}'s digit representation.
 *
 * @author hecvasro
 */
public final class Digit extends NumPadCell<Integer> {
  /**
   * TODO
   */
  private static final List<Digit> INSTANCES;

  /**
   * TODO
   */
  public static final Digit ZERO = new Digit(0);
  /**
   * TODO
   */
  public static final Digit ONE = new Digit(1);
  /**
   * TODO
   */
  public static final Digit TWO = new Digit(2);
  /**
   * TODO
   */
  public static final Digit THREE = new Digit(3);
  /**
   * TODO
   */
  public static final Digit FOUR = new Digit(4);
  /**
   * TODO
   */
  public static final Digit FIVE = new Digit(5);
  /**
   * TODO
   */
  public static final Digit SIX = new Digit(6);
  /**
   * TODO
   */
  public static final Digit SEVEN = new Digit(7);
  /**
   * TODO
   */
  public static final Digit EIGHT = new Digit(8);
  /**
   * TODO
   */
  public static final Digit NINE = new Digit(9);

  static {
    INSTANCES = new ArrayList<>();
    INSTANCES.add(ZERO);
    INSTANCES.add(ONE);
    INSTANCES.add(TWO);
    INSTANCES.add(THREE);
    INSTANCES.add(FOUR);
    INSTANCES.add(FIVE);
    INSTANCES.add(SIX);
    INSTANCES.add(SEVEN);
    INSTANCES.add(EIGHT);
    INSTANCES.add(NINE);
  }

  /**
   * Constructs a digit of a {@link NumPad num pad}.
   *
   * @param value
   *   Cell's value.
   */
  private Digit(@NonNull Integer value) {
    super(value);
  }

  /**
   * Gets the digit associated with the given value.
   *
   * @return Digit associated with the given value.
   */
  public static Digit get(@NonNull Integer value) {
    return INSTANCES.get(value);
  }
}
