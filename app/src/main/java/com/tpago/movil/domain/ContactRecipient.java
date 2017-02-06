package com.tpago.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.domain.util.StringUtils;
import com.tpago.movil.misc.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ContactRecipient extends Recipient {
  private final String phoneNumber;

  public ContactRecipient(@NonNull String phoneNumber, @Nullable String label) {
    super(RecipientType.CONTACT, label);
    this.phoneNumber = phoneNumber;
  }

  public ContactRecipient(@NonNull String phoneNumber) {
    super(RecipientType.CONTACT);
    this.phoneNumber = phoneNumber;
  }

  @NonNull
  @Override
  public String getIdentifier() {
    return phoneNumber;
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
    return super.matches(query) || StringUtils.matches(phoneNumber, query);
  }
}
