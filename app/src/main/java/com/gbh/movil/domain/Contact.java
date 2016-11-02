package com.gbh.movil.domain;

import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Contact representation.
 *
 * @author hecvasro
 */
public class Contact {
  /**
   * Contact's name.
   */
  private final String name;
  /**
   * Contact's phone number.
   */
  private final String phoneNumber;
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
   *   Contact's phone number.
   * @param pictureUri
   *   Contact's picture {@link Uri}.
   */
  public Contact(@NonNull String name, @NonNull String phoneNumber, @NonNull Uri pictureUri) {
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
   * Gets the phone number of the contact.
   *
   * @return Contact's phone number.
   */
  @NonNull
  public final String getPhoneNumber() {
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
}
