package com.tpago.movil;

import android.content.SharedPreferences;

import com.tpago.movil.util.Objects;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class UserStore {
  private static final String FILE_NAME = UserStore.class.getCanonicalName();

  private static final String KEY_PHONE_NUMBER = "phoneNumber";
  private static final String KEY_EMAIL = "email";
  private static final String KEY_FIRST_NAME = "firstName";
  private static final String KEY_LAST_NAME = "lastName";
  private static final String KEY_AVATAR_PATH = "avatarPath";

  private final SharedPreferences sharedPreferences;

  private User user;

  public UserStore(SharedPreferencesCreator sharedPreferencesCreator) {
    if (Objects.isNull(sharedPreferencesCreator)) {
      throw new NullPointerException("Null sharedPreferencesCreator");
    }
    sharedPreferences = sharedPreferencesCreator.create(FILE_NAME);
  }

  private void createUser(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    String avatarPath) {
    user = User.create(phoneNumber, email, firstName, lastName, avatarPath);
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
    user.setOnAvatarPathChangedListener(new User.OnAvatarPathChangedListener() {
      @Override
      public void onAvatarChanged(String avatarPath) {
        Timber.d("onAvatarChanged('%1$s')", avatarPath);
        sharedPreferences.edit()
          .putString(KEY_AVATAR_PATH, avatarPath)
          .apply();
      }
    });
  }

  public final boolean isSet() {
    return sharedPreferences.contains(KEY_PHONE_NUMBER) && sharedPreferences.contains(KEY_EMAIL);
  }

  public final void set(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    String avatarPath) {
    if (!isSet()) {
      createUser(phoneNumber, email, firstName, lastName, avatarPath);
      sharedPreferences.edit()
        .putString(KEY_PHONE_NUMBER, user.getPhoneNumber().getValue())
        .putString(KEY_EMAIL, user.getEmail().getValue())
        .putString(KEY_FIRST_NAME, user.getFirstName())
        .putString(KEY_LAST_NAME, user.getLastName())
        .putString(KEY_AVATAR_PATH, user.getAvatarPath())
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
        sharedPreferences.getString(KEY_AVATAR_PATH, null));
    }
    return user;
  }

  public final void clear() {
    if (isSet()) {
      sharedPreferences.edit()
        .clear()
        .apply();
      if (Objects.isNotNull(user)) {
        user.setOnAvatarPathChangedListener(null);
        user.setOnNameChangedListener(null);
        user = null;
      }
    }
  }
}
