package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;

/**
 * {@link PhoneNumber Phone number} recipient representation.
 *
 * @author hecvasro
 */
public class PhoneNumberRecipient extends Recipient {
  private final PhoneNumber phoneNumber;

  /**
   * Constructs a new {@link PhoneNumber phone number} recipient.
   */
  public PhoneNumberRecipient(@NonNull PhoneNumber phoneNumber, @Nullable String label) {
    super(RecipientType.PHONE_NUMBER, label);
    this.phoneNumber = phoneNumber;
  }

  /**
   * Constructs a new {@link PhoneNumber Phone number} recipient.
   */
  public PhoneNumberRecipient(@NonNull PhoneNumber phoneNumber) {
    super(RecipientType.PHONE_NUMBER);
    this.phoneNumber = phoneNumber;
  }

  @NonNull
  public final PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object)
      && object instanceof PhoneNumberRecipient
      && ((PhoneNumberRecipient) object).getType().equals(getType())
      && ((PhoneNumberRecipient) object).phoneNumber.equals(phoneNumber));
  }

  @Override
  public int hashCode() {
    return Utils.hashCode(getType(), phoneNumber);
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
    return phoneNumber.matches(query);
  }
}
