package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.main.list.ItemHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

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

  public ContactRecipientItemHolder(@NonNull View rootView) {
    super(rootView);
    // Binds all the annotated views and methods.
    ButterKnife.bind(this, rootView);
  }
}
