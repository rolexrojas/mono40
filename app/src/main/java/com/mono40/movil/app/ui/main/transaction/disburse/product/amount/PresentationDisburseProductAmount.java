package com.mono40.movil.app.ui.main.transaction.disburse.product.amount;

import android.net.Uri;

import com.mono40.movil.app.ui.Presentation;

/**
 * @author hecvasro
 */
public interface PresentationDisburseProductAmount extends Presentation {

  void setCurrency(String text);

  void setBalance(String text);

  void setBankLogo(Uri uri);

  void setDestinationProductTypeAndName(String text);

  void setAmount(String text);

  void moveToNextScreen();
}
