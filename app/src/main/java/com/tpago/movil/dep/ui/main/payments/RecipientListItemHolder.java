package com.tpago.movil.dep.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.dep.ui.main.list.ListItemHolder;

import butterknife.BindView;

/**
 * @author hecvasro
 */
class RecipientListItemHolder extends ListItemHolder {
  @BindView(R.id.recipient_picture)
  ImageView recipientPictureImageView;
  @BindView(R.id.recipient_label)
  TextView recipientLabelTextView;
  @BindView(R.id.recipient_extra)
  TextView recipientExtraTextView;
  @BindView(R.id.action_proceed)
  View proceedActionView;
  @BindView(R.id.checkbox_delete)
  CheckBox deleteCheckbox;

  RecipientListItemHolder(@NonNull View rootView, @NonNull OnClickListener listener) {
    super(rootView, listener);
  }
}
