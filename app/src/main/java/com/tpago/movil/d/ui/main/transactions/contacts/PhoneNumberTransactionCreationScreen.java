package com.tpago.movil.d.ui.main.transactions.contacts;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.ui.Screen;

import java.util.List;

/**
 * TODO
 *
 * @author hecvasro
 */
interface PhoneNumberTransactionCreationScreen extends Screen {
  void setPaymentOptions(@NonNull List<Product> paymentOptions);

  void setPaymentOptionCurrency(@NonNull String currency);

  void setPaymentResult(boolean succeeded, String transactionId);
  void showGenericErrorDialog(String message);
  void showGenericErrorDialog();
  void showUnavailableNetworkError();

  void requestPin();

  void requestBankAndAccountNumber();

  void finish();

  void showTransferButtonAsEnabled(boolean showAsEnabled);
}
