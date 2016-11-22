package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.Holder;

import butterknife.BindView;

/**
 * @author hecvasro
 */
class RecipientHolder extends Holder {
  @BindView(R.id.recipient_picture)
  ImageView recipientPictureImageView;
  @BindView(R.id.recipient_label)
  TextView recipientLabelTextView;
  @BindView(R.id.recipient_extra)
  TextView recipientExtraTextView;

  RecipientHolder(@NonNull View rootView, @NonNull OnClickListener listener) {
    super(rootView, listener);
  }
}
