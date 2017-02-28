package com.tpago.movil.dep.ui.main.recipients.contacts;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.dep.ui.main.list.ListItemHolder;

import butterknife.BindView;

/**
 * TODO
 *
 * @author hecvasro
 */
class ContactListItemHolder extends ListItemHolder {
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
  ContactListItemHolder(@NonNull View rootView, @NonNull OnClickListener onClickListener) {
    super(rootView, onClickListener);
  }
}
