package com.tpago.movil.app.upgrade.action;

import android.content.Context;
import android.content.SharedPreferences;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.Email;
import com.tpago.movil.Name;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.session.User;
import com.tpago.movil.store.Store;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

/**
 * @author hecvasro
 */
final class AppUpgradeAction1 extends AppUpgradeAction {

  private static final String PREFS_FILE_NAME = BuildConfig.APPLICATION_ID + ".UserStore";

  private static final String PREF_KEY_PHONE_NUMBER = "phoneNumber";
  private static final String PREF_KEY_EMAIL = "email";
  private static final String PREF_KEY_FIRST_NAME = "firstName";
  private static final String PREF_KEY_LAST_NAME = "lastName";

  static AppUpgradeAction1 create(Context context, Store store) {
    return new AppUpgradeAction1(context, store);
  }

  private final Context context;
  private final Store store;

  private AppUpgradeAction1(Context context, Store store) {
    this.context = ObjectHelper.checkNotNull(context, "context");
    this.store = ObjectHelper.checkNotNull(store, "store");
  }

  @Override
  public int id() {
    return 1;
  }

  @Override
  public void run() throws Exception {
    User user = null;

    // Loads the user from the preferences, if any.
    final SharedPreferences userStorePreferences = this.context
      .getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);

    final String phoneNumber = userStorePreferences
      .getString(PREF_KEY_PHONE_NUMBER, null);
    final String email = userStorePreferences
      .getString(PREF_KEY_EMAIL, null);
    final String firstName = userStorePreferences
      .getString(PREF_KEY_FIRST_NAME, null);
    final String lastName = userStorePreferences
      .getString(PREF_KEY_LAST_NAME, null);

    final boolean isUserSet = !StringHelper.isNullOrEmpty(phoneNumber)
      && !StringHelper.isNullOrEmpty(email)
      && !StringHelper.isNullOrEmpty(firstName)
      && !StringHelper.isNullOrEmpty(lastName);
    if (isUserSet) {
      user = User.builder()
        .id(0)
        .phoneNumber(PhoneNumber.create(phoneNumber))
        .email(Email.create(email))
        .name(Name.create(firstName, lastName))
        .build();
    }

    // Clears the preferences.
    userStorePreferences.edit()
      .clear()
      .apply();

    // Saves the current user to the store, if any.
    if (ObjectHelper.isNotNull(user)) {
      this.store.set("SessionManager.User", user);
    }
  }
}
