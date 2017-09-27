package com.tpago.movil.app.ui.main.settings.index;

import com.tpago.movil.app.ui.Presentation;

/**
 * @author hecvasro
 */
interface SettingsIndexPresentation extends Presentation {

  void setProfileSettingsOptionSecondaryText(String text);

  void setPrimaryPaymentMethodSettingsOptionSecondaryText(String text);

  void setUnlockMethodSettingsOptionSecondaryText(String text);

  void setTimeoutSettingsOptionSecondaryText(String text);

  void setLockOnExitSettingsOptionChecked(boolean checked);
}
