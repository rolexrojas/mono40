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
}
