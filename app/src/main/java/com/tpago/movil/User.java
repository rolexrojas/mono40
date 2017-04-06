package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.tpago.movil.util.Objects;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class User {
  static User create(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    Avatar avatar) {
    final User user = new AutoValue_User(phoneNumber, email, avatar);
    user.setName(firstName, lastName);
    return user;
  }

  private String firstName;
  private String lastName;

  private OnNameChangedListener onNameChangedListener;

  void setOnNameChangedListener(OnNameChangedListener listener) {
    onNameChangedListener = listener;
  }

  public abstract PhoneNumber getPhoneNumber();

  public abstract Email getEmail();

  public abstract Avatar getAvatar();

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
    Preconditions.assertNotNull(firstName, "firstName == null");
    if (Texts.checkIfEmpty(firstName)) {
      throw new IllegalArgumentException("Texts.checkIfEmpty(firstName) == true");
    }
    this.firstName = firstName;
    Preconditions.assertNotNull(lastName, "lastName == null");
    if (Texts.checkIfEmpty(lastName)) {
      throw new IllegalArgumentException("Texts.checkIfEmpty(lastName) == true");
    }
    this.lastName = lastName;
    if (Objects.checkIfNotNull(this.onNameChangedListener)) {
      this.onNameChangedListener.onNameChanged(this.firstName, this.lastName);
    }
  }

  interface OnNameChangedListener {
    void onNameChanged(String firstName, String lastName);
  }
}
