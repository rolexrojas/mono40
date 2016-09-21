package com.tpago.movil.data;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.tpago.movil.R;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class MessageHelper {
  private final Resources resources;

  MessageHelper(@NonNull Resources resources) {
    this.resources = resources;
  }

  @NonNull
  private String getString(@StringRes int resourceId) {
    return resources.getString(resourceId);
  }

  @NonNull
  public final String appName() {
    return getString(R.string.app_name);
  }

  @NonNull
  public final String doneWithExclamationMark() {
    return getString(R.string.done_with_exclamation_mark);
  }

  @NonNull
  public final String goToAccounts() {
    return getString(R.string.go_to_accounts);
  }

  @NonNull
  public final String noInternetConnection() {
    return getString(R.string.no_internet_connection);
  }

  @NonNull
  public final String ok() {
    return getString(R.string.ok);
  }

  @NonNull
  public final String yourAccountHaveBeenAdded() {
    return String.format(getString(R.string.your_account_have_been_added), appName());
  }

  @NonNull
  public final String yourAccountHaveBeenRemoved() {
    return String.format(getString(R.string.your_account_have_been_removed), appName());
  }
}
