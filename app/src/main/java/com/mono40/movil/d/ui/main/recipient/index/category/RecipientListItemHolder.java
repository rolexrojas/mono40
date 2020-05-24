package com.mono40.movil.d.ui.main.recipient.index.category;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mono40.movil.R;
import com.mono40.movil.d.ui.main.list.ListItemHolder;
import com.mono40.movil.d.ui.view.widget.PrefixableTextView;

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
  @BindView(R.id.proceed_action)
  View proceedActionView;
  @BindView(R.id.checkbox_delete)
  CheckBox deleteCheckbox;
  @BindView(R.id.prefixable_text_view_total_owed)
  PrefixableTextView totalOwedPrefixableTextView;
  @BindView(R.id.text_view_due_date)
  TextView dueDateTextView;

  RecipientListItemHolder(@NonNull View rootView, @NonNull OnClickListener listener) {
    super(rootView, listener);
  }
}
