package com.tpago.movil;

import android.content.SharedPreferences;

import com.tpago.movil.content.SharedPreferencesCreator;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class UserStore {
  private static final String KEY_PHONE_NUMBER = "phoneNumber";
  private static final String KEY_EMAIL = "email";
  private static final String KEY_FIRST_NAME = "firstName";
  private static final String KEY_LAST_NAME = "lastName";

  private final SharedPreferences sharedPreferences;
  private final Avatar avatar;

  private User user;

  public UserStore(SharedPreferencesCreator sharedPreferencesCreator, Avatar avatar) {
    this.sharedPreferences = Preconditions
      .checkNotNull(sharedPreferencesCreator, "sharedPreferencesCreator == null")
      .create(UserStore.class.getCanonicalName());
    this.avatar = Preconditions.checkNotNull(avatar, "avatar == null");
  }

  private void createUser(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    Avatar avatar) {
    user = User.create(phoneNumber, email, firstName, lastName, avatar);
    user.setOnNameChangedListener(new User.OnNameChangedListener() {
      @Override
      public void onNameChanged(String firstName, String lastName) {
        Timber.d("onNameChanged('%1$s','%2$s')", firstName, lastName);
        sharedPreferences.edit()
          .putString(KEY_FIRST_NAME, firstName)
          .putString(KEY_LAST_NAME, lastName)
          .apply();
      }
    });
  }

  public final boolean isSet() {
    return sharedPreferences.contains(KEY_PHONE_NUMBER)
      && sharedPreferences.contains(KEY_EMAIL)
      && sharedPreferences.contains(KEY_FIRST_NAME)
      && sharedPreferences.contains(KEY_LAST_NAME);
  }

  public final void set(PhoneNumber phoneNumber, Email email, String firstName, String lastName) {
    if (!isSet()) {
      createUser(phoneNumber, email, firstName, lastName, avatar);
      sharedPreferences.edit()
        .putString(KEY_PHONE_NUMBER, user.getPhoneNumber().getValue())
        .putString(KEY_EMAIL, user.getEmail().getValue())
        .putString(KEY_FIRST_NAME, user.getFirstName())
        .putString(KEY_LAST_NAME, user.getLastName())
        .apply();
    }
  }

  public final User get() {
    if (isSet() && Objects.isNull(user)) {
      createUser(
        PhoneNumber.create(sharedPreferences.getString(KEY_PHONE_NUMBER, null)),
        Email.create(sharedPreferences.getString(KEY_EMAIL, null)),
        sharedPreferences.getString(KEY_FIRST_NAME, null),
        sharedPreferences.getString(KEY_LAST_NAME, null),
        avatar);
    }
    return user;
  }

  public final void clear() {
    if (isSet()) {
      if (Objects.isNotNull(user)) {
        user.setOnNameChangedListener(null);
        user = null;
      }
      avatar.clear();
      sharedPreferences.edit()
        .clear()
        .apply();
    }
  }
}
