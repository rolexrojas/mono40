package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.main.list.HolderBinder;

/**
 * TODO
 *
 * @author hecvasro
 */
abstract class RecipientHolderBinder<T extends Recipient>
  implements HolderBinder<RecipientItem<T>, RecipientHolder> {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  abstract String getExtra(@NonNull T recipient);

  @Override
  public void bind(@NonNull RecipientItem<T> item, @NonNull RecipientHolder holder) {
    final T recipient = item.getRecipient();
    final String label = recipient.getLabel();
    final String extra = getExtra(recipient);
    if (Utils.isNotNull(label)) {
      holder.recipientLabelTextView.setText(label);
      holder.recipientLabelTextView.setGravity(Gravity.START | Gravity.BOTTOM);
      holder.recipientExtraTextView.setText(extra);
      holder.recipientExtraTextView.setGravity(Gravity.START | Gravity.TOP);
      holder.recipientExtraTextView.setVisibility(View.VISIBLE);
    } else {
      holder.recipientLabelTextView.setText(extra);
      holder.recipientLabelTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
      holder.recipientExtraTextView.setText(null);
      holder.recipientExtraTextView.setVisibility(View.GONE);
    }
  }
}
