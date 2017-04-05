package com.tpago.movil.init.unlock;

import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.R;
import com.tpago.movil.Session;
import com.tpago.movil.User;
import com.tpago.movil.UserStore;
import com.tpago.movil.api.DApiBridge;
import com.tpago.movil.api.DApiData;
import com.tpago.movil.api.DApiError;
import com.tpago.movil.app.Presenter;
import com.tpago.movil.init.InitComponent;
import com.tpago.movil.net.HttpResult;
import com.tpago.movil.reactivex.Disposables;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class UnlockPresenter extends Presenter<UnlockPresenter.View> {
  private static String sanitize(String content) {
    return Objects.checkIfNull(content) ? "" : content.trim();
  }

  private String passwordTextInputContent;
  private boolean isPasswordTextInputContentValid = false;

  private Disposable disposable = Disposables.disposed();

  @Inject UserStore userStore;
  @Inject Session.Builder sessionBuilder;
  @Inject
  DApiBridge DApiBridge;

  UnlockPresenter(View view, InitComponent component) {
    super(view);
    // Injects all the annotated dependencies.
    Preconditions.assertNotNull(component, "component == null")
      .inject(this);
  }

  private void updateView() {
    if (isPasswordTextInputContentValid) {
      view.showPasswordTextInputAsErratic(false);
    }
    view.showUnlockButtonAsEnabled(isPasswordTextInputContentValid);
  }

  private void startLoading() {
    view.setUnlockButtonEnabled(false);
    view.showUnlockButtonAsEnabled(false);
    view.startLoading();
  }

  private void stopLoading() {
    view.stopLoading();
    view.showUnlockButtonAsEnabled(isPasswordTextInputContentValid);
    view.setUnlockButtonEnabled(isPasswordTextInputContentValid);
  }

  final void onPasswordTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(passwordTextInputContent)) {
      passwordTextInputContent = sanitizedContent;
      isPasswordTextInputContentValid = Texts.isNotEmpty(passwordTextInputContent);
      updateView();
    }
  }

  final void onUnlockButtonClicked() {
    if (isPasswordTextInputContentValid) {
      final User user = userStore.get();
      final PhoneNumber phoneNumber = user.getPhoneNumber();
      final Email email = user.getEmail();
      disposable = DApiBridge.signIn(phoneNumber, email, passwordTextInputContent, false) // TODO: An endpoint for this scenario is required.
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            startLoading();
          }
        })
        .subscribe(new Consumer<HttpResult<DApiData<String>>>() {
          @Override
          public void accept(HttpResult<DApiData<String>> result) throws Exception {
            stopLoading();
            final DApiData<String> data = result.getData();
            if (result.isSuccessful()) {
              sessionBuilder.setToken(data.getValue());
              view.moveToInitScreen();
            } else {
              final DApiError error = data.getError();
              view.showDialog(
                R.string.error_title,
                error.getDescription(),
                R.string.error_positive_button_text);
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            Timber.e(throwable, "Unlocking");
            stopLoading();
            view.showDialog(
              R.string.error_title,
              R.string.error_generic,
              R.string.error_positive_button_text);
          }
        });
    } else {
      view.showPasswordTextInputAsErratic(true);
      view.showDialog(
        R.string.sign_in_error_title,
        R.string.sign_in_error_description,
        R.string.sign_in_error_positive_button_text);
    }
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();
    final User user = userStore.get();
    view.setAvatarImageContent(user.getAvatar().getFile());
    view.setTitleLabelContent(Texts.join(" ", user.getFirstName(), user.getLastName()));
    view.setPasswordTextInputContent(passwordTextInputContent);
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

    void setAvatarImageContent(File file);

    void setTitleLabelContent(String content);

    void setPasswordTextInputContent(String content);

    void showPasswordTextInputAsErratic(boolean showAsErratic);

    void setUnlockButtonEnabled(boolean enabled);

    void showUnlockButtonAsEnabled(boolean showAsEnabled);

    void startLoading();

    void stopLoading();

    void moveToInitScreen();
  }
}
