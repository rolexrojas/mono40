package com.tpago.movil.onboarding.registration;

import com.tpago.movil.R;
import com.tpago.movil.content.StringResolver;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
final class NameFormPresenter {
  private final RegistrationData data;
  private final StringResolver stringResolver;

  private View view;

  private String firstName;
  private boolean isFirstNameValid = false;
  private String lastName;
  private boolean isLastNameValid = false;

  private static String sanitize(String value) {
    return Objects.isNull(value) ? "" : value.trim();
  }

  NameFormPresenter(RegistrationData data, StringResolver stringResolver) {
    this.data = Preconditions.checkNotNull(data, "data == null");
    this.firstName = sanitize(this.data.getFirstName());
    this.isFirstNameValid = Texts.isNotEmpty(this.firstName);
    this.lastName = sanitize(this.data.getLastName());
    this.isLastNameValid = Texts.isNotEmpty(this.lastName);
    this.stringResolver = Preconditions.checkNotNull(stringResolver, "stringResolver == null");
  }

  private void updateView() {
    if (Objects.isNotNull(view)) {
      if (isFirstNameValid) {
        view.showFirstNameTextInputAsErratic(false);
      }
      if (isLastNameValid) {
        view.showLastNameTextInputAsErratic(false);
      }
      view.showNextButtonAsEnabled(isFirstNameValid && isLastNameValid);
    }
  }

  final void setView(View view) {
    this.view = view;
    if (Objects.isNotNull(this.view)) {
      this.view.setFirstNameTextInputContent(this.firstName);
      this.view.showFirstNameTextInputAsErratic(false);
      this.view.setLastNameTextInputContent(this.lastName);
      this.view.showLastNameTextInputAsErratic(false);
      this.view.showNextButtonAsEnabled(false);
    }
  }

  final void onFirstNameTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!firstName.equals(sanitizedContent)) {
      firstName = sanitizedContent;
      isFirstNameValid = Texts.isNotEmpty(firstName);
      updateView();
    }
  }

  final void onLastNameTextInputContentChanged(String content) {
    final String sanitizedContent = sanitize(content);
    if (!lastName.equals(sanitizedContent)) {
      lastName = sanitizedContent;
      isLastNameValid = Texts.isNotEmpty(lastName);
      updateView();
    }
  }

  final void onNextButtonClicked() {
    if (isFirstNameValid && isLastNameValid) {
      data.setName(firstName, lastName);
      view.moveToNextScreen();
    } else if (Objects.isNotNull(view)) {
      view.showDialog(
        stringResolver.resolve(R.string.name_form_error_title),
        stringResolver.resolve(R.string.name_form_error_message),
        stringResolver.resolve(R.string.name_form_error_positive_button_text));
      view.showFirstNameTextInputAsErratic(!isFirstNameValid);
      view.showLastNameTextInputAsErratic(!isLastNameValid);
    }
  }

  interface View {
    void showDialog(String title, String message, String positiveButtonText);

    void setFirstNameTextInputContent(String content);

    void showFirstNameTextInputAsErratic(boolean showAsErratic);

    void setLastNameTextInputContent(String content);

    void showLastNameTextInputAsErratic(boolean showAsErratic);

    void showNextButtonAsEnabled(boolean showAsEnabled);

    void moveToNextScreen();
  }
}
