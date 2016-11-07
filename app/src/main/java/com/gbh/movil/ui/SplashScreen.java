package com.gbh.movil.ui;

/**
 * Splash screen definition.
 *
 * @author hecvasro
 */
interface SplashScreen {
  /**
   * Finishes the screen, indicating if there're any {@link com.gbh.movil.domain.Account account}
   * additions and/or removals compared to what was saved locally.
   *
   * @param wereAccountAdditions
   *   Indicates if there're any {@link com.gbh.movil.domain.Account account} additions compared to
   *   what was saved locally.
   * @param wereAccountRemovals
   *   Indicates if there're any {@link com.gbh.movil.domain.Account account} removals compared to
   *   what was saved locally.
   */
  void finish(boolean wereAccountAdditions, boolean wereAccountRemovals);
}
