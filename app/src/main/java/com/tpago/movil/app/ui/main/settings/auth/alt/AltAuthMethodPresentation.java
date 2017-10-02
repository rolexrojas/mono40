package com.tpago.movil.app.ui.main.settings.auth.alt;

import com.tpago.movil.app.ui.Presentation;

/**
 * @author hecvasro
 */
public interface AltAuthMethodPresentation extends Presentation {

  void setFingerprintAlhAuthMethodOptionVisibility(boolean visible);

  void setFingerprintOptionSelection(boolean selected);

  void setCodeOptionSelection(boolean selected);

  void setNoneOptionSelection(boolean selected);
}
