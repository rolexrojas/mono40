package com.gbh.movil.domain;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.util.StringHelper;

/**
 * Contact representation.
 *
 * @author hecvasro
 */
public class Contact implements Matchable {
  /**
   * Contact's name.
   */
  private final String name;
  /**
   * Contact's phone number.
   */
  private final PhoneNumber phoneNumber;
  /**
   * Contact's picture {@link Uri}.
   */
  private final Uri pictureUri;

  /**
   * Constructs a new contact.
   *
   * @param name
   *   Contact's name.
   * @param phoneNumber
   *   Contact's {@link PhoneNumber phone number}.
   * @param pictureUri
   *   Contact's picture {@link Uri}.
   */
  public Contact(@NonNull String name, @NonNull PhoneNumber phoneNumber, @NonNull Uri pictureUri) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.pictureUri = pictureUri;
  }

  /**
   * Gets the name of the contact.
   *
   * @return Contact's name.
   */
  @NonNull
  public final String getName() {
    return name;
  }

  /**
   * Gets the {@link PhoneNumber phone number} of the contact.
   *
   * @return Contact's {@link PhoneNumber phone number}.
   */
  @NonNull
  public final PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Gets the picture {@link Uri} of the contact.
   *
   * @return Contact's picture {@link Uri}.
   */
  @NonNull
  public final Uri getPictureUri() {
    return pictureUri;
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
    return Contact.class.getSimpleName() + ":{name='" + name + "',phoneNumber='" + phoneNumber
      + "',pictureUri='" + pictureUri + "'}";
  }

  @Override
  public boolean matches(@Nullable String query) {
    return Utils.isNull(query) || StringHelper.matches(name, query) || phoneNumber.matches(query);
  }
}
