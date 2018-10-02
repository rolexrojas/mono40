package com.tpago.movil.d.data;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.tpago.movil.R;

import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.main.recipient.index.category.Category;

import java.math.BigDecimal;

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
    return getString(R.string.app_name);
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
  public final String bringDeviceCloserToTerminal() {
    return getString(R.string.bring_device_closer_to_the_terminal);
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
  public final String feeForTransaction(@NonNull String currencyCode, @NonNull BigDecimal fee) {
    return String.format(getString(R.string.fee_for_transaction), Formatter.amount(
      currencyCode,
      fee
    ));
  }

  @NonNull
  public final String ok() {
    return getString(R.string.ok);
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
  public final String add(@NonNull String phoneNumber) {
    return String.format(getString(R.string.payments_action_phone_number_add), phoneNumber);
  }
}
