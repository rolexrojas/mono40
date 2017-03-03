package com.tpago.movil.dep.ui.main.transactions.contacts;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.ui.Screen;

import java.util.List;

/**
 * TODO
 *
 * @author hecvasro
 */
interface PhoneNumberTransactionCreationScreen extends Screen {
  void setPaymentOptions(@NonNull List<Product> paymentOptions);

  void setPaymentOptionCurrency(@NonNull String currency);

  void setPaymentResult(boolean succeeded, String message);

  void requestPin();

  void requestBankAndAccountNumber();

  void finish();
}
