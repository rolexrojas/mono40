package com.tpago.movil.init.register;

import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.init.InitData;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Preconditions;

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
    this.initData = Preconditions.assertNotNull(initData, "initData == null");
  }

  final PhoneNumber getPhoneNumber() {
    return initData.getPhoneNumber();
  }

  final PhoneNumber.State getPhoneNumberState() {
    return initData.getPhoneNumberState();
  }

  final String getFirstName() {
    return firstName;
  }

  final String getLastName() {
    return lastName;
  }

  final void setName(String firstName, String lastName) {
    Preconditions.assertNotNull(firstName, "firstName == null");
    if (Texts.isEmpty(firstName)) {
      throw new IllegalArgumentException("Texts.isEmpty(firstName) == true");
    }
    this.firstName = firstName;
    Preconditions.assertNotNull(lastName, "lastName == null");
    if (Texts.isEmpty(lastName)) {
      throw new IllegalArgumentException("Texts.isEmpty(lastName) == true");
    }
    this.lastName = lastName;
  }

  final Email getEmail() {
    return email;
  }

  final void setEmail(Email email) {
    this.email = Preconditions.assertNotNull(email, "email == null");
  }

  final String getPassword() {
    return password;
  }

  final void setPassword(String password) {
    Preconditions.assertNotNull(password, "password == null");
    if (Texts.isEmpty(password)) {
      throw new IllegalArgumentException("Texts.isEmpty(password) == true");
    }
    this.password = password;
  }
}
