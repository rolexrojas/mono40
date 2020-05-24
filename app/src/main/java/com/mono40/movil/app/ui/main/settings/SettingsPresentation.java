package com.mono40.movil.app.ui.main.settings;

import com.mono40.movil.app.ui.Presentation;

/**
 * @author hecvasro
 */
interface SettingsPresentation extends Presentation {

  void setProfileSettingsOptionSecondaryText(String text);

  void setPrimaryPaymentMethodSettingsOptionSecondaryText(String text);

  void setAltAuthMethodOption(String text);

  void setTimeoutSettingsOptionSecondaryText(String text);

  void setLockOnExitSettingsOptionChecked(boolean checked);
}
