package com.mono40.movil.app.ui.main.settings.auth.alt;

import com.mono40.movil.app.ui.Presentation;

/**
 * @author hecvasro
 */
public interface AltAuthMethodPresentation extends Presentation {

  void setFingerprintOptionSelection(boolean selected);

  void setCodeOptionSelection(boolean selected);

  void setNoneOptionSelection(boolean selected);

  void finish();
}
