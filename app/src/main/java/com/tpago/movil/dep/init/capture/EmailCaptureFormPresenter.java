package com.tpago.movil.dep.init.capture;

import com.tpago.movil.Email;
import com.tpago.movil.R;
import com.tpago.movil.dep.content.StringResolver;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

/**
 * @author hecvasro
 */
final class EmailCaptureFormPresenter
  extends CaptureFormPresenter<EmailCaptureFormPresenter.View> {

  private String textInputContent;
  private boolean isTextInputContentValid = false;
  private String confirmationTextInputContent;
  private boolean isConfirmationTextInputContentValid = false;

  private static String sanitize(String content) {
    return StringHelper.emptyIfNull(content)
      .trim();
  }

  EmailCaptureFormPresenter(View view, StringResolver stringResolver, CaptureData captureData) {
    super(view, stringResolver, captureData);
    final Email email = this.captureData.getEmail();
    if (ObjectHelper.isNull(email)) {
      this.textInputContent = null;
      this.isTextInputContentValid = false;
      this.confirmationTextInputContent = null;
      this.isConfirmationTextInputContentValid = false;
    } else {
      this.textInputContent = email.value();
      this.isTextInputContentValid = true;
      this.confirmationTextInputContent = this.textInputContent;
      this.isConfirmationTextInputContentValid = true;
    }
  }

  private boolean checkIfTextInputContentIsValid() {
    return Email.isValid(textInputContent);
  }

  private boolean checkIfConfirmationTextInputContentIsValid() {
    return Email.isValid(confirmationTextInputContent)
      && confirmationTextInputContent.equals(textInputContent);
  }

  private boolean checkIfCanMoveToNextScreen() {
    return checkIfTextInputContentIsValid() && checkIfConfirmationTextInputContentIsValid();
  }

  private void updateView() {
    if (isTextInputContentValid) {
      view.showTextInputContentAsErratic(false);
    }
    if (isConfirmationTextInputContentValid) {
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
      captureData.setEmail(Email.create(textInputContent));
      view.moveToNextScreen();
    } else {
      view.showDialog(
        stringResolver.resolve(R.string.register_form_email_error_title),
        stringResolver.resolve(R.string.register_form_email_error_message),
        stringResolver.resolve(R.string.register_form_email_error_positive_button_text)
      );
      view.showTextInputContentAsErratic(!checkIfTextInputContentIsValid());
      view.showConfirmationTextInputContentAsErratic(!checkIfConfirmationTextInputContentIsValid());
    }
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();
    view.setTextInputContent(textInputContent);
    view.setConfirmationTextInputContent(confirmationTextInputContent);
    updateView();
  }

  interface View extends CaptureFormPresenter.View {

    void setTextInputContent(String content);

    void showTextInputContentAsErratic(boolean showAsErratic);

    void setConfirmationTextInputContent(String content);

    void showConfirmationTextInputContentAsErratic(boolean showAsErratic);
  }
}
