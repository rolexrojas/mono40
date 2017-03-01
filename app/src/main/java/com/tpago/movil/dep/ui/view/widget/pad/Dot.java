package com.tpago.movil.dep.ui.view.widget.pad;

/**
 * {@link DepNumPad Num pad}'s dot representation.
 *
 * @author hecvasro
 */
public final class Dot extends NumPadCell<String> {
  /**
   * TODO
   */
  static final Dot INSTANCE = new Dot();

  /**
   * Constructs the dot of the {@link DepNumPad num pad}.
   */
  protected Dot() {
    super(".");
  }
}
