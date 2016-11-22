package com.gbh.movil.ui.main.payments.recipients.contacts;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.Holder;

import butterknife.BindView;

/**
 * TODO
 *
 * @author hecvasro
 */
class ContactHolder extends Holder {
  @BindView(R.id.contact_picture)
  ImageView pictureImageView;
  @BindView(R.id.contact_name)
  TextView nameTextView;
  @BindView(R.id.contact_phone_number)
  TextView phoneNumberTextView;

  /**
   * TODO
   *
   * @param rootView
   *   TODO
   * @param onClickListener
   *   TODo
   */
  ContactHolder(@NonNull View rootView, @NonNull OnClickListener onClickListener) {
    super(rootView, onClickListener);
  }
}
