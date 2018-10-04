package com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.plan;

import com.tpago.movil.app.ui.Presentation;
import com.tpago.movil.app.ui.main.transaction.item.IndexItem;

import java.util.List;

public interface MicroInsurancePurchasePlanPresentation extends Presentation {

  void setTitle(String text);

  void setSubtitle(String text);

  void setItems(List<IndexItem> items);

  void moveToNextScreen();
}
