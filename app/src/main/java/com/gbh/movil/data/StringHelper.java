package com.gbh.movil.data;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.gbh.movil.R;
import com.gbh.movil.domain.PhoneNumber;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class StringHelper {
  private final Resources resources;

  StringHelper(@NonNull Resources resources) {
    this.resources = resources;
  }

  @NonNull
  private String getString(@StringRes int stringId) {
    return resources.getString(stringId);
  }

  @NonNull
  public final String appName() {
    return getString(R.string.app_name);
  }

  @NonNull
  public final String noResults(@NonNull String query) {
    return String.format(getString(R.string.list_no_results), query);
  }

  @NonNull
  public final String accounts() {
    return getString(R.string.accounts);
  }

  @NonNull
  public final String doneWithExclamationMark() {
    return getString(R.string.done_with_exclamation_mark);
  }

  @NonNull
  public final String feeForTransaction(@NonNull String currencyCode, @NonNull BigDecimal fee) {
    return String.format(getString(R.string.fee_for_transaction), Formatter.amount(currencyCode,
      fee));
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
  public final String payments() {
    return getString(R.string.action_pay);
  }

  @NonNull
  public final String transactionWith(@NonNull PhoneNumber phoneNumber) {
    return String.format(Locale.getDefault(),
      getString(R.string.payments_action_phone_number_transaction), phoneNumber);
  }

  @NonNull
  public final String recentTransactions() {
    return getString(R.string.recent_transactions);
  }

  @NonNull
  public final String yourAccountHaveBeenAdded() {
    return String.format(getString(R.string.your_account_have_been_added), appName());
  }

  @NonNull
  public final String yourAccountHaveBeenRemoved() {
    return String.format(getString(R.string.your_account_have_been_removed), appName());
  }

  @NonNull
  public final String add(@NonNull PhoneNumber phoneNumber) {
    return String.format(getString(R.string.payments_action_phone_number_add), phoneNumber);
  }
}
