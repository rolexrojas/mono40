package com.tpago.movil.app.ui.main.settings;

import com.tpago.movil.app.ui.Presentation;

/**
 * @author hecvasro
 */
interface SettingsPresentation extends Presentation {

  void setProfileSettingsOptionSecondaryText(String text);

  void setPrimaryPaymentMethodSettingsOptionSecondaryText(String text);

  void setAltAuthMethodMethodSettingsOptionSecondaryText(String text);

  void setTimeoutSettingsOptionSecondaryText(String text);

  void setLockOnExitSettingsOptionChecked(boolean checked);
}
