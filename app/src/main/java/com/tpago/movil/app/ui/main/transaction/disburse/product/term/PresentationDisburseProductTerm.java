package com.tpago.movil.app.ui.main.transaction.disburse.product.term;

import com.tpago.movil.app.ui.Presentation;

/**
 * @author hecvasro
 */
public interface PresentationDisburseProductTerm extends Presentation {

  void setDescription(String text);

  void setTerm(String text);

  void moveToNextScreen();
}
