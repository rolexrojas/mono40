package com.tpago.movil.init.register;

import com.tpago.movil.app.Presenter;
import com.tpago.movil.content.StringResolver;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */

abstract class RegisterFormPresenter<V extends RegisterFormPresenter.View> extends Presenter<V> {
  protected final StringResolver stringResolver;
  protected final RegisterData registerData;

  RegisterFormPresenter(V view, StringResolver stringResolver, RegisterData registerData) {
    super(view);
    this.stringResolver = Preconditions.checkNotNull(stringResolver, "stringResolver == null");
    this.registerData = Preconditions.checkNotNull(registerData, "registrationData == null");
  }

  abstract void onMoveToNextScreenButtonClicked();

  interface View extends Presenter.View {
    void showDialog(String title, String message, String positiveButtonText);

    void setMoveToNextScreenButtonEnabled(boolean enabled);

    void showMoveToNextScreenButtonAsEnabled(boolean showAsEnabled);

    void moveToNextScreen();
  }
}
