package com.gbh.movil.domain;

import android.support.annotation.NonNull;

/**
 * {@link Contact} recipient representation.
 *
 * @author hecvasro
 */
public class ContactRecipient extends Recipient {
  private final Contact contact;

  /**
   * Constructs a new {@link Contact} recipient.
   *
   * @param contact
   *   TODO
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
    return super.equals(object) || (object != null && object instanceof ContactRecipient
      && ((ContactRecipient) object).contact.equals(contact));
  }

  @Override
  public int hashCode() {
    return contact.hashCode();
  }

  @Override
  public String toString() {
    return "ContactRecipient:{recipient=" + super.toString() + ",contact=" + contact + "}";
  }
}
