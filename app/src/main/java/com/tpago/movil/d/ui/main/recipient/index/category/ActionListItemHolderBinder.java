package com.tpago.movil.d.ui.main.recipient.index.category;

import android.support.annotation.NonNull;

import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;

/**
 * @author hecvasro
 */
class ActionListItemHolderBinder implements ListItemHolderBinder<Action, ActionListItemHolder> {

  private final StringHelper stringHelper;
  private final Category category;

  ActionListItemHolderBinder(@NonNull StringHelper stringHelper, Category category) {
    this.stringHelper = stringHelper;
    this.category = category;
  }

  @Override
  public void bind(@NonNull Action item, @NonNull ActionListItemHolder holder) {
    String actionText = "";
    switch (item.type()) {
      case ADD_PHONE_NUMBER:
        actionText = stringHelper.add(((PhoneNumberAction) item).phoneNumber().formattedValued());
        break;
      case TRANSACTION_WITH_PHONE_NUMBER:
        actionText = stringHelper.transactionWith(
          category,
          ((PhoneNumberAction) item).phoneNumber().formattedValued()
        );
        break;
    }
    holder.actionTextView.setText(actionText);
  }
}
