package com.mono40.movil.app.upgrade.action;

import android.content.Context;
import android.content.SharedPreferences;

import com.mono40.movil.BuildConfig;
import com.mono40.movil.Email;
import com.mono40.movil.Name;
import com.mono40.movil.PhoneNumber;
import com.mono40.movil.session.User;
import com.mono40.movil.store.DiskStore;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;

/**
 * @author hecvasro
 */
final class AppUpgradeAction1 extends AppUpgradeAction {

  private static final String PREFS_FILE_NAME = BuildConfig.APPLICATION_ID + ".UserStore";

  private static final String PREF_KEY_PHONE_NUMBER = "phoneNumber";
  private static final String PREF_KEY_EMAIL = "email";
  private static final String PREF_KEY_FIRST_NAME = "firstName";
  private static final String PREF_KEY_LAST_NAME = "lastName";

  static AppUpgradeAction1 create(Context context, DiskStore diskStore) {
    return new AppUpgradeAction1(context, diskStore);
  }

  private final Context context;
  private final DiskStore diskStore;

  private AppUpgradeAction1(Context context, DiskStore diskStore) {
    this.context = ObjectHelper.checkNotNull(context, "context");
    this.diskStore = ObjectHelper.checkNotNull(diskStore, "diskStore");
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

    // Saves the current user to the diskStore, if any.
    if (ObjectHelper.isNotNull(user)) {
      this.diskStore.set("SessionManager.User", user)
        .blockingAwait();
    }
  }
}
