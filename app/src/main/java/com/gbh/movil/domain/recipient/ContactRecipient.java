package com.gbh.movil.domain.recipient;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.Recipient;

/**
 * {@link Contact} recipient representation.
 *
 * @author hecvasro
 */
public class ContactRecipient extends Recipient {
  private final Contact contact;

  /**
   * Constructs a new {@link Contact} recipient.
   */
  public ContactRecipient(@NonNull Contact contact) {
    super(RecipientType.CONTACT);
    this.contact = contact;
  }

  @NonNull
  public final Contact getContact() {
    return contact;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof ContactRecipient
      && ((ContactRecipient) object).contact.equals(contact));
  }

  @Override
  public int hashCode() {
    return Utils.hashCode(type, contact);
  }

  @Override
  public String toString() {
    return ContactRecipient.class.getSimpleName() + ":{super=" + super.toString() + ",contact="
      + contact + "}";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean matches(@Nullable String query) {
    return contact.matches(query);
  }
}
