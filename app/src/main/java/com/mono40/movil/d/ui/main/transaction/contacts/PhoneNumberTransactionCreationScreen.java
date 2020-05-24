package com.mono40.movil.d.ui.main.transaction.contacts;

import androidx.annotation.NonNull;

import com.mono40.movil.d.domain.Product;
import com.mono40.movil.d.ui.Screen;

import java.util.List;

/**
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

  void requestCarrier();

  void finish();
}
