package com.tpago.movil.data;

import android.support.annotation.NonNull;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class SchedulerProvider {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Scheduler computation() {
    return Schedulers.computation();
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Scheduler io() {
    return Schedulers.io();
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Scheduler ui() {
    return AndroidSchedulers.mainThread();
  }
}
