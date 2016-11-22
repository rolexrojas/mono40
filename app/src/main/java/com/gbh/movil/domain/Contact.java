package com.gbh.movil.domain;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.util.StringUtils;

import java.io.Serializable;

/**
 * Contact representation.
 *
 * @author hecvasro
 */
public class Contact implements Serializable, Matchable {
  /**
   * Contact's phone number.
   */
  private final PhoneNumber phoneNumber;

  /**
   * Contact's name.
   */
  private String name;
  /**
   * Contact's picture {@link Uri}.
   */
  private Uri pictureUri;

  /**
   * Constructs a new contact.
   *
   * @param phoneNumber
   *   Contact's {@link PhoneNumber phone number}.
   */
  public Contact(@NonNull PhoneNumber phoneNumber) {
    this.phoneNumber = phoneNumber;
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
   * Gets the name of the contact.
   *
   * @return Contact's name.
   */
  @Nullable
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the contact.
   *
   * @param name
   *   Contact's name.
   */
  public void setName(@Nullable String name) {
    this.name = name;
  }

  /**
   * Gets the picture {@link Uri uri} of the contact.
   *
   * @return Contact's picture {@link Uri uri}.
   */
  @Nullable
  public Uri getPictureUri() {
    return pictureUri;
  }

  /**
   * Sets the picture {@link Uri uri} of the contact.
   *
   * @param pictureUri
   *   Contact's picture {@link Uri uri}.
   */
  public void setPictureUri(@Nullable Uri pictureUri) {
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
