package com.mono40.movil.d.ui.main.recipient.index.category;

import com.mono40.movil.R;

/**
 * @author Hector Vasquez
 */
public enum Category {
  PAY(R.string.pay, R.string.invoice),
  TRANSFER(R.string.transfer, R.string.beneficiary),
  RECHARGE(R.string.recharge, R.string.number);

  public final int stringId;
  public final int subjectStringId;

  Category(int stringId, int subjectStringId) {
    this.stringId = stringId;
    this.subjectStringId = subjectStringId;
  }
}
