package com.tpago.movil.init.register;

import com.tpago.movil.R;
import com.tpago.movil.content.StringResolver;
import com.tpago.movil.d.domain.Password;
import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
final class PasswordRegisterFormPresenter extends RegisterFormPresenter<PasswordRegisterFormPresenter.View> {
  private String textInputContent;
  private String confirmationTextInputContent;

  private static String sanitize(String content) {
    return Objects.checkIfNull(content) ? "" : content.trim();
  }

  PasswordRegisterFormPresenter(
    View view,
    StringResolver stringResolver,
    RegisterData registerData) {
    super(view, stringResolver, registerData);
    this.textInputContent = this.registerData.getPassword();
    this.confirmationTextInputContent = this.textInputContent;
  }

  private boolean checkIfTextInputContentValid() {
    return Password.checkIfValid(textInputContent);
  }

  private boolean checkIfConfirmationTextInputContentValid() {
    return Password.checkIfValid(confirmationTextInputContent)
      && confirmationTextInputContent.equals(textInputContent);
  }

  private boolean checkIfCanMoveToNextScreen() {
    return checkIfTextInputContentValid() && checkIfConfirmationTextInputContentValid();
  }

  private void updateView() {
    if (checkIfTextInputContentValid()) {
      view.showTextInputContentAsErratic(false);
    }
    if (checkIfConfirmationTextInputContentValid()) {
      view.showConfirmationTextInputContentAsErratic(false);
    }
    view.showMoveToNextScreenButtonAsEnabled(checkIfCanMoveToNextScreen());
  }

  final void onTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(textInputContent)) {
      textInputContent = sanitizedContent;
      updateView();
    }
  }

  final void onConfirmationTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(confirmationTextInputContent)) {
      confirmationTextInputContent = sanitizedContent;
      updateView();
    }
  }

  @Override
  void onMoveToNextScreenButtonClicked() {
    if (checkIfCanMoveToNextScreen()) {
      registerData.setPassword(textInputContent);
      view.hideKeyboard();
      view.moveToNextScreen();
    } else {
      view.showDialog(
        stringResolver.resolve(R.string.register_form_password_error_title),
        stringResolver.resolve(R.string.register_form_password_error_message),
        stringResolver.resolve(R.string.register_form_password_error_positive_button_text));
      view.showTextInputContentAsErratic(!checkIfTextInputContentValid());
      view.showConfirmationTextInputContentAsErratic(!checkIfConfirmationTextInputContentValid());
    }
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();
    view.setTextInputContent(textInputContent);
    view.setConfirmationTextInputContent(confirmationTextInputContent);
    updateView();
  }

  interface View extends RegisterFormPresenter.View {
    void setTextInputContent(String content);
    void showTextInputContentAsErratic(boolean showAsErratic);
    void setConfirmationTextInputContent(String content);
    void showConfirmationTextInputContentAsErratic(boolean showAsErratic);

    void hideKeyboard();
  }
}
