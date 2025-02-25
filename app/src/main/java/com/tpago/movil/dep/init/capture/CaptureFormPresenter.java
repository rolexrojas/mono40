package com.tpago.movil.dep.init.capture;

import com.tpago.movil.dep.Presenter;
import com.tpago.movil.dep.content.StringResolver;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */

abstract class CaptureFormPresenter<V extends CaptureFormPresenter.View> extends Presenter<V> {

  protected final StringResolver stringResolver;
  protected final CaptureData captureData;

  CaptureFormPresenter(V view, StringResolver stringResolver, CaptureData captureData) {
    super(view);
    this.stringResolver = ObjectHelper.checkNotNull(stringResolver, "stringResolver");
    this.captureData = ObjectHelper.checkNotNull(captureData, "registrationData");
  }

  abstract void onMoveToNextScreenButtonClicked();

  interface View extends Presenter.View {

    void showDialog(String title, String message, String positiveButtonText);

    void setMoveToNextScreenButtonEnabled(boolean enabled);

    void showMoveToNextScreenButtonAsEnabled(boolean showAsEnabled);

    void moveToNextScreen();
  }
}
