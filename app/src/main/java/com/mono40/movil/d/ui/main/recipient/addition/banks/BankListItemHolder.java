package com.mono40.movil.d.ui.main.recipient.addition.banks;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mono40.movil.R;
import com.mono40.movil.d.ui.main.list.ListItemHolder;

import butterknife.BindView;

/**
 * @author hecvasro
 */
class BankListItemHolder extends ListItemHolder {

  @BindView(R.id.image_view_background) ImageView imageView;
  @BindView(R.id.text_view) TextView textView;

  BankListItemHolder(@NonNull View rootView, @NonNull OnClickListener onClickListener) {
    super(rootView, onClickListener);
  }
}
