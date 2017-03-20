package com.tpago.movil.dep.ui.main.recipients.partners;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.dep.ui.main.list.ListItemHolder;

import butterknife.BindView;

/**
 * @author hecvasro
 */
class PartnerListItemHolder extends ListItemHolder {
  @BindView(R.id.image_view_background) ImageView imageView;
  @BindView(R.id.text_view) TextView textView;

  PartnerListItemHolder(@NonNull View rootView, @NonNull OnClickListener onClickListener) {
    super(rootView, onClickListener);
  }
}
