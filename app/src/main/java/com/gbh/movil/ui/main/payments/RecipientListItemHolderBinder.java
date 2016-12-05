package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.gbh.movil.misc.Utils;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.main.list.ListItemHolderBinder;

/**
 * TODO
 *
 * @author hecvasro
 */
class RecipientListItemHolderBinder implements ListItemHolderBinder<Recipient, RecipientListItemHolder> {
  @Override
  public void bind(@NonNull Recipient item, @NonNull RecipientListItemHolder holder) {
    final String label = item.getLabel();
    final String identifier = item.getIdentifier();
    if (Utils.isNotNull(label)) {
      holder.recipientLabelTextView.setText(label);
      holder.recipientLabelTextView.setGravity(Gravity.START | Gravity.BOTTOM);
      holder.recipientExtraTextView.setText(identifier);
      holder.recipientExtraTextView.setGravity(Gravity.START | Gravity.TOP);
      holder.recipientExtraTextView.setVisibility(View.VISIBLE);
    } else {
      holder.recipientLabelTextView.setText(identifier);
      holder.recipientLabelTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
      holder.recipientExtraTextView.setText(null);
      holder.recipientExtraTextView.setVisibility(View.GONE);
    }
  }
}
