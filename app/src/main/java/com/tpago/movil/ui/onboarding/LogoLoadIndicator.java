package com.tpago.movil.ui.onboarding;

import com.tpago.movil.ui.LoadIndicator;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
class LogoLoadIndicator implements LoadIndicator {
  private final LogoView logoView;

  LogoLoadIndicator(LogoView logoView) {
    this.logoView = Preconditions.checkNotNull(logoView, "logo == null");
  }

  @Override
  public void start() {
    logoView.start();
  }

  @Override
  public void stop() {
    logoView.stop();
  }
}
