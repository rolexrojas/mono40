package com.tpago.movil.app;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public abstract class ActivityModule<A extends BaseActivity> {
  protected final A activity;

  protected ActivityModule(A activity) {
    this.activity = Preconditions.checkNotNull(activity, "activity == null");
  }
}
