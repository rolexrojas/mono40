package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.ui.main.list.ItemHolderBinder;

/**
 * TODO
 *
 * @author hecvasro
 */
class ActionItemHolderBinder implements ItemHolderBinder<ActionItem, ActionItemHolder> {
  private final StringHelper stringHelper;

  /**
   * TODO
   *
   * @param stringHelper
   *   TODO
   */
  ActionItemHolderBinder(@NonNull StringHelper stringHelper) {
    this.stringHelper = stringHelper;
  }

  @Override
  public void bind(@NonNull ActionItem item, @NonNull ActionItemHolder holder) {
    String actionText = "";
    switch (item.getType()) {
      case ActionType.ADD_PHONE_NUMBER:
        actionText = stringHelper.add(((PhoneNumberActionItem) item).getPhoneNumber());
        break;
      case ActionType.TRANSACTION_WITH_PHONE_NUMBER:
        actionText = stringHelper.transactionWith(((PhoneNumberActionItem) item).getPhoneNumber());
        break;
    }
    holder.actionTextView.setText(actionText);
  }
}
