package com.tpago.movil.dep.init.register;

import com.tpago.movil.Email;
import com.tpago.movil.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.dep.init.InitData;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

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
    this.initData = ObjectHelper.checkNotNull(initData, "initData");
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
    ObjectHelper.checkNotNull(firstName, "firstName");
    if (StringHelper.isNullOrEmpty(firstName)) {
      throw new IllegalArgumentException("StringHelper.isNullOrEmpty(firstName)");
    }
    this.firstName = firstName;
    ObjectHelper.checkNotNull(lastName, "lastName");
    if (StringHelper.isNullOrEmpty(lastName)) {
      throw new IllegalArgumentException("StringHelper.isNullOrEmpty(lastName)");
    }
    this.lastName = lastName;
  }

  final Email getEmail() {
    return email;
  }

  final void setEmail(Email email) {
    this.email = ObjectHelper.checkNotNull(email, "email");
  }

  final String getPassword() {
    return password;
  }

  final void setPassword(String password) {
    ObjectHelper.checkNotNull(password, "password");
    if (!Password.isValid(password)) {
      throw new IllegalArgumentException("!Password.isValid(password)");
    }
    this.password = password;
  }
}
