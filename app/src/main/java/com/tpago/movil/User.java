package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.tpago.movil.util.Objects;
import com.tpago.movil.text.Texts;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class User {
  private String firstName;
  private String lastName;
  private String avatarPath;

  private OnNameChangedListener onNameChangedListener;
  private OnAvatarPathChangedListener onAvatarPathChangedListener;

  static User create(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    String avatarPath) {
    final User user = new AutoValue_User(phoneNumber, email);
    user.setName(firstName, lastName);
    user.setAvatarPath(avatarPath);
    return user;
  }

  void setOnNameChangedListener(OnNameChangedListener listener) {
    onNameChangedListener = listener;
  }

  void setOnAvatarPathChangedListener(OnAvatarPathChangedListener listener) {
    onAvatarPathChangedListener = listener;
  }

  public abstract PhoneNumber getPhoneNumber();

  public abstract Email getEmail();

  public final String getFirstName() {
    return firstName;
  }

  public final String getLastName() {
    return lastName;
  }

  public final String getName() {
    return Texts.join(" ", firstName, lastName);
  }

  public final void setName(String firstName, String lastName) {
    this.firstName = Texts.nullIfEmpty(firstName);
    this.lastName = Texts.nullIfEmpty(lastName);
    if (Objects.isNotNull(this.onNameChangedListener)) {
      this.onNameChangedListener.onNameChanged(this.firstName, this.lastName);
    }
  }

  public final String getAvatarPath() {
    return avatarPath;
  }

  public final void setAvatarPath(String avatarPath) {
    this.avatarPath = Texts.nullIfEmpty(avatarPath);
    if (Objects.isNotNull(this.onAvatarPathChangedListener)) {
      this.onAvatarPathChangedListener.onAvatarChanged(this.avatarPath);
    }
  }

  interface OnNameChangedListener {
    void onNameChanged(String firstName, String lastName);
  }

  interface OnAvatarPathChangedListener {
    void onAvatarChanged(String avatarPath);
  }
}
