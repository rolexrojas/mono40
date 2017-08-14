package com.tpago.movil.d.ui.main.recipient.index.category;

import com.tpago.movil.R;

/**
 * @author Hector Vasquez
 */
public enum Category {
  PAY(R.string.pay, R.string.invoice),
  TRANSFER(R.string.transfer, R.string.beneficiary),
  RECHARGE(R.string.recharge, R.string.number);

  public final int stringId;
  public final int deletionStringId;

  Category(int stringId, int deletionStringId) {
    this.stringId = stringId;
    this.deletionStringId = deletionStringId;
  }
}
