package com.tpago.movil.ui.onboarding.registration;

import com.tpago.movil.Digit;
import com.tpago.movil.Digits;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hecvasro
 */
final class PhoneNumberValidationPresenter {
  private final RegistrationData data;

  private View view;

  private boolean isPhoneNumberValid = false;
  private List<Digit> phoneNumberDigits = new ArrayList<>();

  PhoneNumberValidationPresenter(RegistrationData data) {
    this.data = Preconditions.checkNotNull(data, "data == null");
  }

  private void update() {
    final String phoneNumber = Digits.stringify(phoneNumberDigits);
    isPhoneNumberValid = PhoneNumber.isValid(phoneNumber);
    if (Objects.isNotNull(view)) {
      view.setText(phoneNumber);
      view.setErraticStateEnabled(!isPhoneNumberValid);
    }
    if (isPhoneNumberValid) {
      // TODO: Start status retrieval.
    }
  }

  final void setView(View view) {
    this.view = view;
    if (Objects.isNull(this.view)) {
      this.phoneNumberDigits.clear();
    } else if (Objects.isNotNull(this.data.getPhoneNumber())) {
      this.phoneNumberDigits.addAll(Digits.getDigits(this.data.getPhoneNumber()));
      this.update();
    }
  }

  final void addDigit(Digit digit) {
    if (!isPhoneNumberValid) {
      phoneNumberDigits.add(digit);
      update();
    }
  }

  final void removeDigit() {
    if (!phoneNumberDigits.isEmpty()) {
      phoneNumberDigits.remove(phoneNumberDigits.size() - 1);
      update();
    }
  }

  public interface View {
    void setText(String text);

    void setErraticStateEnabled(boolean erraticStateEnabled);
  }
}
