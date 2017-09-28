package com.tpago.movil.init.register;

import com.tpago.movil.domain.Email;
import com.tpago.movil.domain.PhoneNumber;
import com.tpago.movil.d.domain.Password;
import com.tpago.movil.init.InitData;
import com.tpago.movil.text.Texts;

import static com.tpago.movil.util.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
final class RegisterData {
  private final InitData initData;

  private String firstName;
  private String lastName;
  private Email email;
  private String password;

  RegisterData(InitData initData) {
    this.initData = assertNotNull(initData, "initData == null");
  }

  final PhoneNumber getPhoneNumber() {
    return initData.getPhoneNumber();
  }

  @PhoneNumber.State
  final int getPhoneNumberState() {
    return initData.getPhoneNumberState();
  }

  final String getFirstName() {
    return firstName;
  }

  final String getLastName() {
    return lastName;
  }

  final void setName(String firstName, String lastName) {
    assertNotNull(firstName, "firstName == null");
    if (Texts.checkIfEmpty(firstName)) {
      throw new IllegalArgumentException("Texts.checkIfEmpty(firstName) == true");
    }
    this.firstName = firstName;
    assertNotNull(lastName, "lastName == null");
    if (Texts.checkIfEmpty(lastName)) {
      throw new IllegalArgumentException("Texts.checkIfEmpty(lastName) == true");
    }
    this.lastName = lastName;
  }

  final Email getEmail() {
    return email;
  }

  final void setEmail(Email email) {
    this.email = assertNotNull(email, "email == null");
  }

  final String getPassword() {
    return password;
  }

  final void setPassword(String password) {
    assertNotNull(password, "password == null");
    if (!Password.checkIfValid(password)) {
      throw new IllegalArgumentException("Password.checkIfValid(password) == false");
    }
    this.password = password;
  }
}
