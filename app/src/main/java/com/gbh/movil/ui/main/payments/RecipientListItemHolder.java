package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ListItemHolder;

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

  RecipientListItemHolder(@NonNull View rootView, @NonNull OnClickListener listener) {
    super(rootView, listener);
  }
}
