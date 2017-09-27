package com.tpago.movil;

import static com.tpago.movil.util.Objects.checkIfNotNull;
import static com.tpago.movil.util.Objects.checkIfNull;
import static com.tpago.movil.util.Preconditions.assertNotNull;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tpago.movil.User.OnIdChangedListener;
import com.tpago.movil.User.OnNameChangedListener;
import com.tpago.movil.User.OnCarrierChangedListener;
import com.tpago.movil.api.UserData;
import com.tpago.movil.content.SharedPreferencesCreator;
import com.tpago.movil.util.ObjectHelper;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hecvasro
 */
@Deprecated
public final class UserStore
  implements OnIdChangedListener, OnNameChangedListener, OnCarrierChangedListener {

  private static final String KEY_ID = "id";
  private static final String KEY_PHONE_NUMBER = "phoneNumber";
  private static final String KEY_EMAIL = "email";
  private static final String KEY_FIRST_NAME = "firstName";
  private static final String KEY_LAST_NAME = "lastName";
  private static final String KEY_CARRIER = "carrier";

  private final Gson gson;
  private final SharedPreferences sharedPreferences;
  private final Avatar avatar;

  private AtomicReference<User> user = new AtomicReference<>();

  public UserStore(Gson gson, SharedPreferencesCreator sharedPreferencesCreator, Avatar avatar) {
    this.gson = ObjectHelper.checkNotNull(gson, "gson");
    this.sharedPreferences = assertNotNull(
      sharedPreferencesCreator,
      "sharedPreferencesCreator == null"
    )
      .create(UserStore.class.getCanonicalName());
    this.avatar = assertNotNull(avatar, "avatar == null");
  }

  public final boolean isSet() {
    return this.sharedPreferences.contains(KEY_ID)
      && this.sharedPreferences.contains(KEY_PHONE_NUMBER)
      && this.sharedPreferences.contains(KEY_EMAIL)
      && this.sharedPreferences.contains(KEY_FIRST_NAME)
      && this.sharedPreferences.contains(KEY_LAST_NAME);
  }

  public final void set(UserData userData) {
    final User reference = this.get();
    if (checkIfNotNull(reference)) {
      reference.id(userData.id());
      reference.name(userData.firstName(), userData.lastName());
    } else {
      this.sharedPreferences.edit()
        .putInt(KEY_ID, userData.id())
        .putString(KEY_PHONE_NUMBER, userData.phoneNumber())
        .putString(KEY_EMAIL, userData.email())
        .putString(KEY_FIRST_NAME, userData.firstName())
        .putString(KEY_LAST_NAME, userData.lastName())
        .apply();
    }
  }

  public final User get() {
    if (checkIfNull(this.user.get()) && this.isSet()) {
      final User reference = User.createBuilder()
        .phoneNumber(PhoneNumber.create(this.sharedPreferences.getString(KEY_PHONE_NUMBER, null)))
        .email(Email.create(this.sharedPreferences.getString(KEY_EMAIL, null)))
        .avatar(this.avatar)
        .build();

      reference.id(this.sharedPreferences.getInt(KEY_ID, 0));
      reference.onIdChangedListener(this);

      reference.name(
        this.sharedPreferences.getString(KEY_FIRST_NAME, null),
        this.sharedPreferences.getString(KEY_LAST_NAME, null)
      );
      reference.onNameChangedListener(this);

      if (this.sharedPreferences.contains(KEY_CARRIER)) {
        reference.carrier(
          this.gson.fromJson(
            this.sharedPreferences.getString(KEY_CARRIER, null),
            Partner.class
          )
        );
      }
      reference.onCarrierChangedListener(this);

      this.user.set(reference);
    }
    return this.user.get();
  }

  public final void clear() {
    if (this.isSet()) {
      final User reference = this.user.get();
      if (checkIfNotNull(reference)) {
        reference.onIdChangedListener(null);
        reference.onNameChangedListener(null);
        reference.onCarrierChangedListener(null);
      }

      this.user.set(null);
      this.sharedPreferences.edit()
        .clear()
        .apply();
    }
  }

  @Override
  public void onIdChanged(int id) {
    if (this.isSet()) {
      this.sharedPreferences.edit()
        .putInt(KEY_ID, id)
        .apply();
    }
  }

  @Override
  public void onNameChanged(String firstName, String lastName) {
    if (this.isSet()) {
      this.sharedPreferences.edit()
        .putString(KEY_FIRST_NAME, firstName)
        .putString(KEY_LAST_NAME, lastName)
        .apply();
    }
  }

  @Override
  public void onCarrierChanged(Partner carrier) {
    if (this.isSet()) {
      this.sharedPreferences.edit()
        .putString(KEY_CARRIER, this.gson.toJson(carrier, Partner.class))
        .apply();
    }
  }
}
