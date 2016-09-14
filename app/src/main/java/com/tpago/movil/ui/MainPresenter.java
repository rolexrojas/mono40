package com.tpago.movil.ui;

import android.support.annotation.NonNull;

import com.tpago.movil.domain.InitialDataLoader;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class MainPresenter {
  /**
   * TODO
   */
  private final MainScreen screen;

  /**
   * TODO
   */
  private final InitialDataLoader initialDataLoader;

  /**
   * TODO
   *
   * @param screen
   *   TODO
   */
  public MainPresenter(@NonNull MainScreen screen, @NonNull InitialDataLoader initialDataLoader) {
    this.screen = screen;
    this.initialDataLoader = initialDataLoader;
  }

  /**
   * TODO
   */
  public final void start() {
    // TODO
  }

  /**
   * TODO
   */
  public final void stop() {
    // TODO
  }
}
