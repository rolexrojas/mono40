package com.mono40.movil.d.ui.main.transaction;

import com.mono40.movil.R;
import com.mono40.movil.d.ui.main.recipient.index.category.Category;

/**
 * @author Hector Vasquez
 */
public enum TransactionCategory {
  PAY(R.string.pay),
  TRANSFER(R.string.transfer),
  RECHARGE(R.string.recharge),
  DISBURSE(R.string.withdraw);

  public static TransactionCategory transform(Category category) {
    if (category == Category.PAY) {
      return PAY;
    } else if (category == Category.TRANSFER) {
      return TRANSFER;
    } else if (category == Category.RECHARGE) {
      return RECHARGE;
    } else {
      return DISBURSE;
    }
  }

  public final int stringId;

  TransactionCategory(int stringId) {
    this.stringId = stringId;
  }
}
