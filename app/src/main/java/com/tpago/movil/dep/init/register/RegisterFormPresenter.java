package com.tpago.movil.dep.init.register;

import com.tpago.movil.dep.Presenter;
import com.tpago.movil.dep.content.StringResolver;
import com.tpago.movil.dep.Preconditions;

/**
 * @author hecvasro
 */

abstract class RegisterFormPresenter<V extends RegisterFormPresenter.View> extends Presenter<V> {
  protected final StringResolver stringResolver;
  protected final RegisterData registerData;

  RegisterFormPresenter(V view, StringResolver stringResolver, RegisterData registerData) {
    super(view);
    this.stringResolver = Preconditions.assertNotNull(stringResolver, "stringResolver == null");
    this.registerData = Preconditions.assertNotNull(registerData, "registrationData == null");
  }

  abstract void onMoveToNextScreenButtonClicked();

  interface View extends Presenter.View {
    void showDialog(String title, String message, String positiveButtonText);

    void setMoveToNextScreenButtonEnabled(boolean enabled);

    void showMoveToNextScreenButtonAsEnabled(boolean showAsEnabled);

    void moveToNextScreen();
  }
}
