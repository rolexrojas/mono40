package com.tpago.movil.d.ui.main.payments;

import android.support.annotation.NonNull;

import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;

/**
 * TODO
 *
 * @author hecvasro
 */
class ActionListItemHolderBinder implements ListItemHolderBinder<Action, ActionListItemHolder> {
  private final StringHelper stringHelper;

  /**
   * TODO
   *
   * @param stringHelper
   *   TODO
   */
  ActionListItemHolderBinder(@NonNull StringHelper stringHelper) {
    this.stringHelper = stringHelper;
  }

  @Override
  public void bind(@NonNull Action item, @NonNull ActionListItemHolder holder) {
    String actionText = "";
    switch (item.getType()) {
      case ActionType.ADD_PHONE_NUMBER:
        actionText = stringHelper.add(((PhoneNumberAction) item).getPhoneNumber());
        break;
      case ActionType.TRANSACTION_WITH_PHONE_NUMBER:
        actionText = stringHelper.transactionWith(((PhoneNumberAction) item).getPhoneNumber());
        break;
    }
    holder.actionTextView.setText(actionText);
  }
}
