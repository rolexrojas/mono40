package com.tpago.movil.dep.init.register;

import com.tpago.movil.dep.Presenter;
import com.tpago.movil.dep.content.StringResolver;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */

abstract class RegisterFormPresenter<V extends RegisterFormPresenter.View> extends Presenter<V> {

  protected final StringResolver stringResolver;
  protected final RegisterData registerData;

  RegisterFormPresenter(V view, StringResolver stringResolver, RegisterData registerData) {
    super(view);
    this.stringResolver = ObjectHelper.checkNotNull(stringResolver, "stringResolver");
    this.registerData = ObjectHelper.checkNotNull(registerData, "registrationData");
  }

  abstract void onMoveToNextScreenButtonClicked();

  interface View extends Presenter.View {

    void showDialog(String title, String message, String positiveButtonText);

    void setMoveToNextScreenButtonEnabled(boolean enabled);

    void showMoveToNextScreenButtonAsEnabled(boolean showAsEnabled);

    void moveToNextScreen();
  }
}
