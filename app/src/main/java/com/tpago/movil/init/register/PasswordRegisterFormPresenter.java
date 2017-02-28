package com.tpago.movil.init.register;

import com.tpago.movil.R;
import com.tpago.movil.content.StringResolver;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
final class PasswordRegisterFormPresenter
  extends RegisterFormPresenter<PasswordRegisterFormPresenter.View> {
  private String textInputContent;
  private boolean isTextInputContentValid = false;
  private String confirmationTextInputContent;
  private boolean isConfirmationTextInputContentValid = false;

  private static String sanitize(String content) {
    return Objects.isNull(content) ? "" : content.trim();
  }

  PasswordRegisterFormPresenter(View view, StringResolver stringResolver, RegisterData registerData) {
    super(view, stringResolver, registerData);
    this.textInputContent = this.registerData.getPassword();
    this.isTextInputContentValid = Texts.isNotEmpty(this.textInputContent);
    this.confirmationTextInputContent = this.textInputContent;
    this.isConfirmationTextInputContentValid = this.isTextInputContentValid;
  }

  private void updateView() {
    if (isTextInputContentValid) {
      view.showTextInputContentAsErratic(false);
    }
    if (isConfirmationTextInputContentValid) {
      view.showConfirmationTextInputContentAsErratic(false);
    }
    view.showMoveToNextScreenButtonAsEnabled(isTextInputContentValid
      && isConfirmationTextInputContentValid);
  }

  final void onTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(textInputContent)) {
      textInputContent = sanitizedContent;
      isTextInputContentValid = Texts.isNotEmpty(textInputContent);
      updateView();
    }
  }

  final void onConfirmationTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(confirmationTextInputContent)) {
      confirmationTextInputContent = sanitizedContent;
      isConfirmationTextInputContentValid = Texts.isNotEmpty(confirmationTextInputContent)
        && confirmationTextInputContent.equals(textInputContent);
      updateView();
    }
  }

  @Override
  void onMoveToNextScreenButtonClicked() {
    if (isTextInputContentValid && isConfirmationTextInputContentValid) {
      registerData.setPassword(textInputContent);
      view.moveToNextScreen();
    } else {
      view.showDialog(
        stringResolver.resolve(R.string.register_form_password_error_title),
        stringResolver.resolve(R.string.register_form_password_error_message),
        stringResolver.resolve(R.string.register_form_password_error_positive_button_text));
      view.showTextInputContentAsErratic(!isTextInputContentValid);
      view.showConfirmationTextInputContentAsErratic(!isConfirmationTextInputContentValid);
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
  }
}
