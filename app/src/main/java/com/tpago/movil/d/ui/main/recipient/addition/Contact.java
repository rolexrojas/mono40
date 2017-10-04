package com.tpago.movil.d.ui.main.recipient.addition;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.misc.Utils;
import com.tpago.movil.d.data.UriUtils;
import com.tpago.movil.d.domain.Matchable;
import com.tpago.movil.d.domain.PhoneNumber;
import com.tpago.movil.d.domain.util.StringUtils;

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
   * Contact's updateName.
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
   *   Contact's updateName.
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
   * Gets the updateName of the contact.
   *
   * @return Contact's updateName.
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
    return Contact.class.getSimpleName() + ":{phoneNumber='" + phoneNumber + "',updateName='" + name
      + "',pictureUri='" + pictureUri + "'}";
  }

  @Override
  public boolean matches(@Nullable String query) {
    return phoneNumber.matches(query) || StringUtils.matches(name, query);
  }
}
