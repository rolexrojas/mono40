package com.mono40.movil.d.data;

import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.mono40.movil.R;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.Recipient;

import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.ui.main.recipient.index.category.Category;
import java.math.BigDecimal;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public final class StringHelper {
  private final Resources resources;

  StringHelper(@NonNull Resources resources) {
    this.resources = resources;
  }

  @NonNull
  private String getString(@StringRes int id) {
    return resources.getString(id);
  }

  @NonNull
  private String format(@StringRes int id, @NonNull String... args) {
    return String.format(getString(id), args);
  }

  public final String resolve(@StringRes int id) {
    return getString(id);
  }

  @NonNull
  public final String appName() {
    return "tPago";
  }

  @NonNull
  public final String incorrectFormat() {
    return getString(R.string.incorrect_format);
  }

  @NonNull
  public final String isRequired() {
    return getString(R.string.is_required);
  }

  @NonNull
  public final String doesNotMatch() {
    return getString(R.string.does_not_match);
  }

  @NonNull
  public final String dialogProductAdditionTitle() {
    return getString(R.string.dialog_product_addition_title);
  }

  @NonNull
  public final String dialogProductAdditionMessage() {
    return format(R.string.dialog_product_addition_message, appName());
  }

  @NonNull
  public final String dialogProductAdditionPositiveAction() {
    return getString(R.string.dialog_product_addition_positive_action);
  }

  @NonNull
  public final String dialogProductAdditionNegativeAction() {
    return getString(R.string.dialog_product_addition_negative_action);
  }

  @NonNull
  public final String dialogNfcDefaultAssignationTitle() {
    return format(R.string.dialog_nfc_default_assignation_title, appName());
  }

  @NonNull
  public final String dialogNfcDefaultAssignationMessage() {
    return format(R.string.dialog_nfc_default_assignation_message, appName());
  }

  @NonNull
  public final String dialogNfcDefaultAssignationPositiveAction() {
    return getString(R.string.dialog_nfc_default_assignation_positive_action);
  }

  @NonNull
  public final String dialogNfcDefaultAssignationNegativeAction() {
    return getString(R.string.dialog_nfc_default_assignation_negative_action);
  }

  @NonNull
  public final String bringDeviceCloserToTerminal() {
    return getString(R.string.bring_device_closer_to_the_terminal);
  }

  @NonNull
  public final String noInternetConnection() {
    return getString(R.string.no_internet_connection);
  }

  @NonNull
  public final String cannotProcessYourRequestAtTheMoment() {
    return getString(R.string.cannot_process_your_request_at_the_moment);
  }

  @Nullable
  public final String recipientAdditionConfirmationMessage(@NonNull Recipient recipient) {
    return format(R.string.format_recipient_addition_message_contact, recipient.getIdentifier());
  }

  @NonNull
  public final String transactionCreationConfirmationTitle() {
    return getString(R.string.transactionSucceeded);
  }

  @Nullable
  public final String transactionCreationConfirmationMessage(@NonNull Recipient recipient) {
    switch (recipient.getType()) {
      case PHONE_NUMBER:
        return format(R.string.format_transaction_confirmation_message_phone_number,
          recipient.getIdentifier());
      default:
        return null;
    }
  }

  @NonNull
  public final String productNumber(@NonNull Product product) {
    return product.getNumber().replaceAll("[^\\d]", "");
  }

  @NonNull
  public final String maskedProductNumber(@NonNull Product product) {
    final String format;
    if (Product.checkIfCreditCard(product)) {
      format = getString(R.string.format_productNumber_masked_creditCard);
    } else {
      format = getString(R.string.format_productNumber_masked);
    }
    return String.format(format, productNumber(product));
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
  public final String ok() {
    return getString(R.string.ok);
  }

  @NonNull
  public final String payments() {
    return getString(R.string.action_pay);
  }

  @NonNull
  public final String transactionWith(Category category, String phoneNumber) {
    return String.format(
      getString(R.string.payments_action_phone_number_transaction),
      getString(category.stringId),
      phoneNumber
    );
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
  public final String add(@NonNull String phoneNumber) {
    return String.format(getString(R.string.payments_action_phone_number_add), phoneNumber);
  }
}
