package com.tpago.movil.ui.main.payments;

/**
 * Action representation.
 *
 * @author hecvasro
 */
abstract class Action {
  /**
   * Action's {@link ActionType type}.
   */
  @ActionType
  private final int type;

  /**
   * Constructs a new action.
   *
   * @param type
   *   Action's {@link ActionType type}.
   */
  Action(@ActionType int type) {
    this.type = type;
  }

  /**
   * Gets the {@link ActionType type} of the action.
   *
   * @return Action's {@link ActionType type}.
   */
  @ActionType
  final int getType() {
    return type;
  }
}
