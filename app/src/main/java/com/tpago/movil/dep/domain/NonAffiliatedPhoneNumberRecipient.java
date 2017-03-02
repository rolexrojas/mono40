package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.*;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
public class NonAffiliatedPhoneNumberRecipient extends Recipient {
  private final String phoneNumber;

  private Bank bank;
  private String accountNumber;
  private Product product;

  public NonAffiliatedPhoneNumberRecipient(
    @NonNull String phoneNumber,
    @Nullable String label,
    Bank bank,
    String accountNumber,
    Product product) {
    super(RecipientType.NON_AFFILIATED_PHONE_NUMBER, label);
    this.phoneNumber = phoneNumber;
    this.bank = bank;
    this.accountNumber = accountNumber;
    this.product = product;
  }

  public NonAffiliatedPhoneNumberRecipient(@NonNull String phoneNumber, @Nullable String label) {
    this(phoneNumber, label, null, null, null);
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

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @Override
  public String toString() {
    return PhoneNumberRecipient.class.getSimpleName() + ":{super=" + super.toString()
      + ",phoneNumber='" + phoneNumber + "'"
      + ",bank=" + bank.toString()
      + ",accountNumber='" + accountNumber + "'"
      + ",product=" + product
      + "}";
  }

  @Override
  public String getId() {
    return Texts.join("-", getType(), phoneNumber, bank.getId(), accountNumber);
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return com.tpago.movil.PhoneNumber.format(phoneNumber);
  }
}
