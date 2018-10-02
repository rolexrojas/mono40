package com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.confirm;

import com.tpago.movil.app.ui.Presentation;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.transaction.TransactionSummary;

import java.util.List;

public interface MicroInsurancePurchaseConfirmPresentation extends Presentation {

  void setCurrency(String text);

  void setCoverage(String text);

  void setPremium(String text);

  void setTerm(String text);

  void setTotal(String text);

  void setDueDate(String text);

  void setPaymentMethods(List<Product> paymentMethods);

  void requestPin(String text);

  void finish(TransactionSummary summary);
}
