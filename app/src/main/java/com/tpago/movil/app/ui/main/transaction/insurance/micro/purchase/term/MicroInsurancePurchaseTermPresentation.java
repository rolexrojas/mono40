package com.tpago.movil.app.ui.main.transaction.insurance.micro.purchase.term;

import com.tpago.movil.app.ui.Presentation;
import com.tpago.movil.insurance.micro.MicroInsurancePlan;

import java.util.List;

public interface MicroInsurancePurchaseTermPresentation extends Presentation {

  void setTitle(String text);

  void setSubtitle(String text);

  void setTerms(List<MicroInsurancePlan.Term> terms, int selectedTermIndex);

  void moveToNextScreen();
}
