package com.gbh.movil.ui.main.payments.recipients.contacts;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.Matchable;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.util.StringUtils;

/**
 * Contact representation.
 *
 * @author hecvasro
 */
class Contact implements Matchable {
  /**
   * Contact's phone number.
   */
  final PhoneNumber phoneNumber;
  /**
   * Contact's name.
   */
  final String name;
  /**
   * Contact's picture {@link Uri}.
   */
  final Uri pictureUri;

  /**
   * Constructs a new contact.
   *
   * @param phoneNumber
   *   Contact's {@link PhoneNumber phone number}.
   */
  Contact(@NonNull PhoneNumber phoneNumber, @NonNull String name, @Nullable Uri pictureUri) {
    this.phoneNumber = phoneNumber;
    this.name = name;
    this.pictureUri = pictureUri;
  }

  @Override
  public boolean equals(Object object) {
    return super.equals(object) || (Utils.isNotNull(object) && object instanceof Contact
      && ((Contact) object).phoneNumber.equals(phoneNumber));
  }

  @Override
  public int hashCode() {
    return phoneNumber.hashCode();
  }

  @Override
  public String toString() {
    return Contact.class.getSimpleName() + ":{phoneNumber='" + phoneNumber + "',name='" + name
      + "',pictureUri='" + pictureUri + "'}";
  }

  @Override
  public boolean matches(@Nullable String query) {
    return phoneNumber.matches(query) || StringUtils.matches(name, query);
  }
}
