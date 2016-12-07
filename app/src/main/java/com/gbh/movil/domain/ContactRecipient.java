package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.misc.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ContactRecipient extends Recipient {
  private final PhoneNumber phoneNumber;

  public ContactRecipient(@NonNull PhoneNumber phoneNumber, @Nullable String label) {
    super(RecipientType.CONTACT, label);
    this.phoneNumber = phoneNumber;
  }

  public ContactRecipient(@NonNull PhoneNumber phoneNumber) {
    super(RecipientType.CONTACT);
    this.phoneNumber = phoneNumber;
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return phoneNumber.toString();
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object)
      && object instanceof ContactRecipient
      && ((ContactRecipient) object).getType().equals(getType())
      && ((ContactRecipient) object).phoneNumber.equals(phoneNumber));
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
    return super.matches(query) || phoneNumber.matches(query);
  }
}
