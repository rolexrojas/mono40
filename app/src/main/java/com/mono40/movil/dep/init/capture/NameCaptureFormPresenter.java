package com.mono40.movil.dep.init.capture;

import com.mono40.movil.R;
import com.mono40.movil.dep.content.StringResolver;
import com.mono40.movil.dep.text.Texts;
import com.mono40.movil.util.StringHelper;

/**
 * @author hecvasro
 */
final class NameCaptureFormPresenter
  extends CaptureFormPresenter<NameCaptureFormPresenter.View> {

  private String firstName;
  private boolean isFirstNameValid = false;
  private String lastName;
  private boolean isLastNameValid = false;

  private static String sanitize(String value) {
    return StringHelper.emptyIfNull(value)
      .trim();
  }

  NameCaptureFormPresenter(
    NameCaptureFormPresenter.View view,
    StringResolver stringResolver,
    CaptureData captureData
  ) {
    super(view, stringResolver, captureData);
    this.firstName = sanitize(this.captureData.getFirstName());
    this.isFirstNameValid = Texts.checkIfNotEmpty(this.firstName);
    this.lastName = sanitize(this.captureData.getLastName());
    this.isLastNameValid = Texts.checkIfNotEmpty(this.lastName);
  }

  private void updateView() {
    if (isFirstNameValid) {
      view.showFirstNameTextInputAsErratic(false);
    }
    if (isLastNameValid) {
      view.showLastNameTextInputAsErratic(false);
    }
    view.showMoveToNextScreenButtonAsEnabled(isFirstNameValid && isLastNameValid);
  }

  final void onFirstNameTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!firstName.equals(sanitizedContent)) {
      firstName = sanitizedContent;
      isFirstNameValid = Texts.checkIfNotEmpty(firstName);
      updateView();
    }
  }

  final void onLastNameTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!lastName.equals(sanitizedContent)) {
      lastName = sanitizedContent;
      isLastNameValid = Texts.checkIfNotEmpty(lastName);
      updateView();
    }
  }

  @Override
  void onMoveToNextScreenButtonClicked() {
    if (isFirstNameValid && isLastNameValid) {
      captureData.setName(firstName, lastName);
      view.moveToNextScreen();
    } else {
      view.showDialog(
        stringResolver.resolve(R.string.register_form_name_error_title),
        stringResolver.resolve(R.string.register_form_name_error_message),
        stringResolver.resolve(R.string.register_form_name_error_positive_button_text)
      );
      view.showFirstNameTextInputAsErratic(!isFirstNameValid);
      view.showLastNameTextInputAsErratic(!isLastNameValid);
    }
  }

  @Override
  public void onViewStarted() {
    super.onViewStarted();
    view.setFirstNameTextInputContent(firstName);
    view.setLastNameTextInputContent(lastName);
    updateView();
  }

  interface View extends CaptureFormPresenter.View {

    void setFirstNameTextInputContent(String content);

    void showFirstNameTextInputAsErratic(boolean showAsErratic);

    void setLastNameTextInputContent(String content);

    void showLastNameTextInputAsErratic(boolean showAsErratic);
  }
}
