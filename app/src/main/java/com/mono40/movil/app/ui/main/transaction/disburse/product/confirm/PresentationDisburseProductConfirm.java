package com.mono40.movil.app.ui.main.transaction.disburse.product.confirm;

import com.mono40.movil.app.ui.Presentation;
import com.mono40.movil.transaction.TransactionSummary;

/**
 * @author hecvasro
 */
public interface PresentationDisburseProductConfirm extends Presentation {

  void setCurrency(String text);

  void setAmount(String text);

  void setTerm(String text);

  void setRate(String text);

  void setInsurance(String text);

  void setBalance(String text);

  void setFee(String text);

  @Deprecated
  void requestPin(String text);

  void finish(TransactionSummary summary);
}
