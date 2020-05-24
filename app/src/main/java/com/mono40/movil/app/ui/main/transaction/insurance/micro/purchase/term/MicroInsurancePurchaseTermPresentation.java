package com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.term;

import com.mono40.movil.app.ui.Presentation;
import com.mono40.movil.insurance.micro.MicroInsurancePlan;

import java.util.List;

public interface MicroInsurancePurchaseTermPresentation extends Presentation {

  void setTitle(String text);

  void setSubtitle(String text);

  void setTerms(List<MicroInsurancePlan.Term> terms, int selectedTermIndex);

  void moveToNextScreen();
}
