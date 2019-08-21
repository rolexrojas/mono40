package com.tpago.movil.app.upgrade.action;

import androidx.annotation.NonNull;

import io.reactivex.functions.Action;

/**
 * @author hecvasro
 */
public abstract class AppUpgradeAction implements Action, Comparable<AppUpgradeAction> {

  public abstract int id();

  @Override
  public String toString() {
    return String.format("AppUpgradeAction{id=%1$s}", this.id());
  }

  @Override
  public boolean equals(Object o) {
    return o == this || (o instanceof AppUpgradeAction && this.id() == ((AppUpgradeAction) o).id());
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.id();
    return h;
  }

  @Override
  public int compareTo(@NonNull AppUpgradeAction that) {
    return this.id() - that.id();
  }
}
