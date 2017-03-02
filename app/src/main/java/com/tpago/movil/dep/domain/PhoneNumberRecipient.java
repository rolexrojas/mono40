package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.dep.domain.util.StringUtils;
import com.tpago.movil.text.Texts;

/**
 * Phone number recipient representation.
 *
 * @author hecvasro
 */
public class PhoneNumberRecipient extends Recipient {
  private final String phoneNumber;

  /**
   * Constructs a new phone number recipient.
   */
  public PhoneNumberRecipient(@NonNull String phoneNumber, @Nullable String label) {
    super(RecipientType.PHONE_NUMBER, label);
    this.phoneNumber = phoneNumber;
  }

  /**
   * Constructs a new phone number recipient.
   */
  public PhoneNumberRecipient(@NonNull String phoneNumber) {
    super(RecipientType.PHONE_NUMBER);
    this.phoneNumber = phoneNumber;
  }

  @NonNull
  public final String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public String getId() {
    return Texts.join("-", getType(), phoneNumber);
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return phoneNumber;
  }

  @Override
  public String toString() {
    return PhoneNumberRecipient.class.getSimpleName() + ":{super=" + super.toString()
      + ",phoneNumber='" + phoneNumber + "'}";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean matches(@Nullable String query) {
    return super.matches(query) || StringUtils.matches(phoneNumber, query);
  }
}
