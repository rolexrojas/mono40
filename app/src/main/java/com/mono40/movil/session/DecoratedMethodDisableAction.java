package com.mono40.movil.session;

import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
final class DecoratedMethodDisableAction implements UnlockMethodDisableAction {

  static DecoratedMethodDisableAction create(
    UnlockMethodDisableAction methodDisableAction,
    UnlockMethodDisableAction keyStoreDisableAction
  ) {
    return new DecoratedMethodDisableAction(methodDisableAction, keyStoreDisableAction);
  }

  private final UnlockMethodDisableAction methodDisableAction;
  private final UnlockMethodDisableAction keyStoreDisableAction;

  private DecoratedMethodDisableAction(
    UnlockMethodDisableAction methodDisableAction,
    UnlockMethodDisableAction keyStoreDisableAction
  ) {
    this.methodDisableAction = ObjectHelper
      .checkNotNull(methodDisableAction, "methodDisableAction");
    this.keyStoreDisableAction = ObjectHelper
      .checkNotNull(keyStoreDisableAction, "keyStoreDisableAction");
  }

  @Override
  public void run() throws Exception {
    this.methodDisableAction.run();
    this.keyStoreDisableAction.run();
  }
}
