package com.tpago.movil.app.ui.main.transaction.paypal.confirm;

import android.net.Uri;

import com.tpago.movil.app.ui.Presentation;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.transaction.TransactionSummary;

import java.util.List;

public interface PayPalTransactionConfirmPresentation extends Presentation {

  void setLogo(Uri uri);

  void setAccountAlias(String text);

  void setAccountCurrency(String text);

  void setPaymentMethods(List<Product> paymentMethods);

  void setAmount(String text);

  void requestPin(String text);

  void finish(TransactionSummary summary);
}
