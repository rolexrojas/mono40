package com.gbh.movil.ui.main.payments;

import com.gbh.movil.ui.main.list.Item;

/**
 * Action {@link Item item} representation.
 *
 * @author hecvasro
 */
abstract class ActionItem implements Item {
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
  ActionItem(@ActionType int type) {
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
