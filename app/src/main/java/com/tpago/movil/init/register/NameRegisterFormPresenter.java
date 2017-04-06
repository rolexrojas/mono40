package com.tpago.movil.init.register;

import com.tpago.movil.R;
import com.tpago.movil.content.StringResolver;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
final class NameRegisterFormPresenter extends RegisterFormPresenter<NameRegisterFormPresenter.View> {
  private String firstName;
  private boolean isFirstNameValid = false;
  private String lastName;
  private boolean isLastNameValid = false;

  private static String sanitize(String value) {
    return Objects.checkIfNull(value) ? "" : value.trim();
  }

  NameRegisterFormPresenter(
    NameRegisterFormPresenter.View view,
    StringResolver stringResolver,
    RegisterData registerData) {
    super(view, stringResolver, registerData);
    this.firstName = sanitize(this.registerData.getFirstName());
    this.isFirstNameValid = Texts.checkIfNotEmpty(this.firstName);
    this.lastName = sanitize(this.registerData.getLastName());
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
      registerData.setName(firstName, lastName);
      view.moveToNextScreen();
    } else {
      view.showDialog(
        stringResolver.resolve(R.string.register_form_name_error_title),
        stringResolver.resolve(R.string.register_form_name_error_message),
        stringResolver.resolve(R.string.register_form_name_error_positive_button_text));
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

  interface View extends RegisterFormPresenter.View {
    void setFirstNameTextInputContent(String content);

    void showFirstNameTextInputAsErratic(boolean showAsErratic);

    void setLastNameTextInputContent(String content);

    void showLastNameTextInputAsErratic(boolean showAsErratic);
  }
}
