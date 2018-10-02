package com.tpago.movil.session;

import com.tpago.movil.util.ObjectHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hecvasro
 */
final class UnlockMethodDisableActionFactory {

  static Builder builder() {
    return new Builder();
  }

  private final Map<UnlockMethod, UnlockMethodDisableAction> actions;

  private UnlockMethodDisableActionFactory(Builder builder) {
    this.actions = builder.actions;
  }

  final UnlockMethodDisableAction make(UnlockMethod method) {
    ObjectHelper.checkNotNull(method, "method");
    if (!this.actions.containsKey(method)) {
      throw new IllegalArgumentException(String.format("!this.actions.containsType(\"%1$s\")", method));
    }
    return this.actions.get(method);
  }

  static final class Builder {

    private final Map<UnlockMethod, UnlockMethodDisableAction> actions;

    private Builder() {
      this.actions = new HashMap<>();
    }

    final Builder addAction(UnlockMethod method, UnlockMethodDisableAction action) {
      ObjectHelper.checkNotNull(method, "method");
      ObjectHelper.checkNotNull(action, "action");
      this.actions.put(method, action);
      return this;
    }

    final UnlockMethodDisableActionFactory build() {
      return new UnlockMethodDisableActionFactory(this);
    }
  }
}
