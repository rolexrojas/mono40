package com.mono40.movil.app.ui.main.settings.profile.change.password;

import com.mono40.movil.app.ui.Presentation;

/**
 * @author hecvasro
 */
public interface PresentationChangePassword extends Presentation {

  void showPasswordInputAsErratic(boolean showAsErratic);

  void showValueInputAsErratic(boolean showAsErratic);

  void showValueConfirmationAsErratic(boolean showAsErratic);

  void showSubmitButtonAsEnabled(boolean showAsEnabled);
}
