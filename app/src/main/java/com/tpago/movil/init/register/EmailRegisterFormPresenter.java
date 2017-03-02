package com.tpago.movil.init.register;

import com.tpago.movil.Email;
import com.tpago.movil.R;
import com.tpago.movil.content.StringResolver;
import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
final class EmailRegisterFormPresenter
  extends RegisterFormPresenter<EmailRegisterFormPresenter.View> {
  private String textInputContent;
  private boolean isTextInputContentValid = false;
  private String confirmationTextInputContent;
  private boolean isConfirmationTextInputContentValid = false;

  private static String sanitize(String content) {
    return Objects.isNull(content) ? "" : content.trim();
  }

  EmailRegisterFormPresenter(View view, StringResolver stringResolver, RegisterData registerData) {
    super(view, stringResolver, registerData);
    final Email email = this.registerData.getEmail();
    if (Objects.isNull(email)) {
      this.textInputContent = null;
      this.isTextInputContentValid = false;
      this.confirmationTextInputContent = null;
      this.isConfirmationTextInputContentValid = false;
    } else {
      this.textInputContent = email.getValue();
      this.isTextInputContentValid = true;
      this.confirmationTextInputContent = this.textInputContent;
      this.isConfirmationTextInputContentValid = true;
    }
  }

  private boolean canMoveToNextScreen() {
    return isTextInputContentValid
      && isConfirmationTextInputContentValid
      && textInputContent.equals(confirmationTextInputContent);
  }

  private void updateView() {
    if (isTextInputContentValid) {
      view.showTextInputContentAsErratic(false);
    }
    if (isConfirmationTextInputContentValid) {
      view.showConfirmationTextInputContentAsErratic(false);
    }
    view.showMoveToNextScreenButtonAsEnabled(canMoveToNextScreen());
  }

  final void onTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(textInputContent)) {
      textInputContent = sanitizedContent;
      isTextInputContentValid = Email.isValid(textInputContent);
      updateView();
    }
  }

  final void onConfirmationTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!sanitizedContent.equals(confirmationTextInputContent)) {
      confirmationTextInputContent = sanitizedContent;
      isConfirmationTextInputContentValid = Email.isValid(confirmationTextInputContent);
      updateView();
    }
  }

  @Override
  void onMoveToNextScreenButtonClicked() {
    if (canMoveToNextScreen()) {
      registerData.setEmail(Email.create(textInputContent));
      view.moveToNextScreen();
    } else {
      view.showDialog(
        stringResolver.resolve(R.string.register_form_email_error_title),
        stringResolver.resolve(R.string.register_form_email_error_message),
        stringResolver.resolve(R.string.register_form_email_error_positive_button_text));
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
