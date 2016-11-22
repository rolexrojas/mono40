package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.data.StringHelper;
import com.gbh.movil.ui.main.list.HolderBinder;

/**
 * TODO
 *
 * @author hecvasro
 */
class ActionHolderBinder implements HolderBinder<Action, ActionHolder> {
  private final StringHelper stringHelper;

  /**
   * TODO
   *
   * @param stringHelper
   *   TODO
   */
  ActionHolderBinder(@NonNull StringHelper stringHelper) {
    this.stringHelper = stringHelper;
  }

  @Override
  public void bind(@NonNull Action item, @NonNull ActionHolder holder) {
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
