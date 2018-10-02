package com.tpago.movil.dep.init.capture;

import com.tpago.movil.Email;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.dep.init.InitData;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

/**
 * @author hecvasro
 */
final class CaptureData {

  private final InitData initData;

  private String firstName;
  private String lastName;
  private Email email;
  private Bank bank;
  private String phoneNumber;

  CaptureData(InitData initData) {
    this.initData = ObjectHelper.checkNotNull(initData, "initData");
  }

  final String getPhoneNumber() {
    return this.phoneNumber;
  }

  final void setPhoneNumber(String number) { this.phoneNumber = number; }

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
    return this.email;
  }

  final void setEmail(Email email) {
    this.email = ObjectHelper.checkNotNull(email, "email");
  }

  final Bank getBank() {
    return this.bank;
  }

  final void setBank(Bank bank) {
    this.bank = ObjectHelper.checkNotNull(bank, "bank");
  }

  final void onDestroy() {
  }
}
