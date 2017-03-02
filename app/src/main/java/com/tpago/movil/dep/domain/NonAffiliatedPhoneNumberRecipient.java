package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.Bank;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
public class NonAffiliatedPhoneNumberRecipient extends Recipient {
  private final String phoneNumber;

  private Bank bank;
  private String accountNumber;

  public NonAffiliatedPhoneNumberRecipient(
    @NonNull String phoneNumber,
    @Nullable String label,
    Bank bank,
    String accountNumber) {
    super(RecipientType.NON_AFFILIATED_PHONE_NUMBER, label);
    this.phoneNumber = phoneNumber;
    this.bank = bank;
    this.accountNumber = accountNumber;
  }

  public NonAffiliatedPhoneNumberRecipient(@NonNull String phoneNumber, @Nullable String label) {
    this(phoneNumber, label, null, null);
  }

  public NonAffiliatedPhoneNumberRecipient(@NonNull String phoneNumber) {
    this(phoneNumber, null);
  }

  public final String getPhoneNumber() {
    return phoneNumber;
  }

  public boolean canBeTransferTo() {
    return Objects.isNotNull(bank) && Texts.isNotEmpty(accountNumber);
  }

  public Bank getBank() {
    return bank;
  }

  public void setBank(Bank bank) {
    this.bank = bank;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  @Override
  public String toString() {
    return PhoneNumberRecipient.class.getSimpleName() + ":{super=" + super.toString()
      + ",phoneNumber='" + phoneNumber + "'"
      + ",bank=" + bank.toString()
      + ",accountNumber='" + accountNumber + "'"
      + "}";
  }

  @Override
  public String getId() {
    return Texts.join("-", getType(), phoneNumber, bank.getId(), accountNumber);
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return phoneNumber;
  }
}
