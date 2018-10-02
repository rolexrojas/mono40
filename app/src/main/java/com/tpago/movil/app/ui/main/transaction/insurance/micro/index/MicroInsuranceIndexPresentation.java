package com.tpago.movil.app.ui.main.transaction.insurance.micro.index;

import com.tpago.movil.app.ui.Presentation;
import com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchase;
import com.tpago.movil.app.ui.main.transaction.item.IndexItem;

import java.util.List;

public interface MicroInsuranceIndexPresentation extends Presentation {

  void setItems(List<IndexItem> items);

  void startTransaction(MicroInsurancePurchase purchase);
}
