package com.gbh.movil.ui.main.recipients;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.misc.Utils;
import com.gbh.movil.data.UriUtils;
import com.gbh.movil.domain.Matchable;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.util.StringUtils;

/**
 * Contact representation.
 *
 * @author hecvasro
 */
public class Contact implements Matchable {
  /**
   * Contact's phone number.
   */
  private final PhoneNumber phoneNumber;
  /**
   * Contact's name.
   */
  private final String name;
  /**
   * Contact's picture {@link Uri}.
   */
  private final Uri pictureUri;

  /**
   * Constructs a new contact.
   *
   * @param phoneNumber
   *   Contact's {@link PhoneNumber phone number}.
   * @param name
   *   Contact's name.
   * @param pictureUri
   *   Contact's picture {@link Uri uri}.
   */
  public Contact(@NonNull PhoneNumber phoneNumber, @NonNull String name, @Nullable Uri pictureUri) {
    this.phoneNumber = phoneNumber;
    this.name = name;
    this.pictureUri = UriUtils.getUriOrEmpty(pictureUri);
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
  @NonNull
  public final String getName() {
    return name;
  }

  /**
   * Gets the picture {@link Uri uri} of the contact.
   *
   * @return Contact's picture {@link Uri uri}.
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
    return Contact.class.getSimpleName() + ":{phoneNumber='" + phoneNumber + "',name='" + name
      + "',pictureUri='" + pictureUri + "'}";
  }

  @Override
  public boolean matches(@Nullable String query) {
    return phoneNumber.matches(query) || StringUtils.matches(name, query);
  }
}
