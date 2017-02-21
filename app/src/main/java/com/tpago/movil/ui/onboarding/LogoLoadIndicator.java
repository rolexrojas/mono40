package com.tpago.movil.ui.onboarding;

import com.tpago.movil.ui.LoadIndicator;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
class LogoLoadIndicator implements LoadIndicator {
  private final Logo logo;

  LogoLoadIndicator(Logo logo) {
    this.logo = Preconditions.checkNotNull(logo, "logo == null");
  }

  @Override
  public void start() {
    logo.start();
  }

  @Override
  public void stop() {
    logo.stop();
  }
}
