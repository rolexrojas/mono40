package com.mono40.movil.app.ui.main.transaction.disburse.product.term;

import com.mono40.movil.app.ui.Presentation;

/**
 * @author hecvasro
 */
public interface PresentationDisburseProductTerm extends Presentation {

  void setDescription(String text);

  void setTerm(String text);

  void moveToNextScreen();
}
