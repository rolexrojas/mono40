package com.mono40.movil.app.ui.init.unlock.ChangePassword;

import com.mono40.movil.app.ui.Presentation;

/**
 * Created by solucionesgbh on 6/12/18.
 */

public interface ChangePasswordPresentation extends Presentation {

    void showNewPasswordTextInputContentAsErratic(boolean showAsErratic);

    void showPasswordConfirmationTextInputContentAsErratic(boolean showAsErratic);

    boolean isNewPasswordInputContent();

    boolean isConfirmationPasswordInputContent();

    void finish();
}