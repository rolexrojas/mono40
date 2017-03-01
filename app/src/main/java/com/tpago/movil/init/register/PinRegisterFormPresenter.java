package com.tpago.movil.init.register;

import com.tpago.movil.Digit;
import com.tpago.movil.Pin;
import com.tpago.movil.R;
import com.tpago.movil.Session;
import com.tpago.movil.UserStore;
import com.tpago.movil.api.ApiBridge;
import com.tpago.movil.api.ApiData;
import com.tpago.movil.api.ApiError;
import com.tpago.movil.app.Presenter;
import com.tpago.movil.net.HttpResult;
import com.tpago.movil.reactivex.Disposables;
import com.tpago.movil.util.Preconditions;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class PinRegisterFormPresenter extends Presenter<PinRegisterFormPresenter.View> {
  private final Pin.Builder builder;

  private Disposable disposable = Disposables.disposed();

  @Inject UserStore userStore;
  @Inject ApiBridge apiBridge;
  @Inject Session.Builder sessionBuilder;

  @Inject RegisterData registerData;

  PinRegisterFormPresenter(View view, RegisterComponent component) {
    super(view);
    // Injects all annotated dependencies.
    Preconditions.checkNotNull(component, "component == null")
      .inject(this);
    // Initializes the presenter.
    builder = new Pin.Builder();
  }

  private void updateView() {
    view.setTextInputContent(builder.getMaskedValue());
    if (builder.canBuild()) {
      final Pin pin = builder.build();
      disposable = apiBridge.signUp(
        registerData.getPhoneNumber(),
        registerData.getEmail(),
        registerData.getPassword(),
        pin)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            view.startLoading();
          }
        })
        .subscribe(new Consumer<HttpResult<ApiData<String>>>() {
          @Override
          public void accept(HttpResult<ApiData<String>> result) throws Exception {
            view.stopLoading();
            final ApiData<String> apiData = result.getData();
            if (result.isSuccessful()) {
              userStore.set(
                registerData.getPhoneNumber(),
                registerData.getEmail(),
                registerData.getFirstName(),
                registerData.getLastName());
              sessionBuilder.setToken(apiData.getValue());
              view.moveToNextScreen();
            } else {
              final ApiError error = apiData.getError();
              view.showDialog(
                R.string.error_title,
                error.getDescription(),
                R.string.error_positive_button_text);
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            Timber.e(throwable, "Registering an user");
            view.stopLoading();
            view.showDialog(
              R.string.error_title,
              R.string.error_message,
              R.string.error_positive_button_text);
          }
        });
    }
  }

  final void onDigitButtonClicked(Digit digit) {
    builder.addDigit(digit);
    updateView();
  }

  final void onDeleteButtonClicked() {
    builder.removeLastDigit();
    updateView();
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();
    updateView();
  }

  @Override
  public void onViewStopped() {
    super.onViewStopped();
    Disposables.dispose(disposable);
  }

  interface View extends Presenter.View {
    void showDialog(int titleId, String message, int positiveButtonTextId);

    void showDialog(int titleId, int messageId, int positiveButtonTextId);

    void setTextInputContent(String content);

    void startLoading();

    void stopLoading();

    void moveToNextScreen();
  }
}
