package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.dep.domain.util.StringUtils;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.text.Texts;

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
