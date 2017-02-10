package com.tpago.movil.d.ui.view.widget.pad;

/**
 * {@link NumPad Num pad}'s dot representation.
 *
 * @author hecvasro
 */
public final class Dot extends NumPadCell<String> {
  /**
   * TODO
   */
  static final Dot INSTANCE = new Dot();

  /**
   * Constructs the dot of the {@link NumPad num pad}.
   */
  protected Dot() {
    super(".");
  }
}
