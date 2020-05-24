package com.mono40.movil.dep.init.register;

import androidx.annotation.Nullable;

import com.mono40.movil.Email;
import com.mono40.movil.lib.Password;
import com.mono40.movil.PhoneNumber;
import com.mono40.movil.dep.init.InitData;
import com.mono40.movil.io.FileHelper;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;

import java.io.File;

/**
 * @author hecvasro
 */
final class RegisterData {

  private final InitData initData;

  private String firstName;
  private String lastName;
  private Email email;
  private String password;

  private File picture;

  private boolean submitted = false;

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

  @Nullable
  final File getPicture() {
    return this.picture;
  }

  final void setPicture(File picture) {
    this.picture = ObjectHelper.checkNotNull(picture, "picture");
  }

  public final void setSubmitted(boolean submitted) {
    this.submitted = submitted;
  }

  final void onDestroy() {
    if (ObjectHelper.isNotNull(this.picture) && !this.submitted) {
      FileHelper.deleteFile(this.picture);
    }
  }
}
