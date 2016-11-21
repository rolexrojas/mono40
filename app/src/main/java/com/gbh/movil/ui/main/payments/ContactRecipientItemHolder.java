package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ItemHolder;

import butterknife.BindView;

/**
 * @author hecvasro
 */
class ContactRecipientItemHolder extends ItemHolder {
  @BindView(R.id.contact_picture)
  ImageView contactPictureImageView;
  @BindView(R.id.contact_name)
  TextView contactNameTextView;
  @BindView(R.id.contact_phone_number)
  TextView contactPhoneNumberTextView;

  ContactRecipientItemHolder(@NonNull View rootView, @NonNull OnClickListener listener) {
    super(rootView, listener);
  }
}
