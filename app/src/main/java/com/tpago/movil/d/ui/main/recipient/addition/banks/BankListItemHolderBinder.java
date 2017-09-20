package com.tpago.movil.d.ui.main.recipient.addition.banks;

import android.support.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;
import com.tpago.movil.domain.Bank;
import com.tpago.movil.domain.LogoStyle;

/**
 * TODO
 *
 * @author hecvasro
 */
class BankListItemHolderBinder implements ListItemHolderBinder<Bank, BankListItemHolder> {

  @Override
  public void bind(@NonNull Bank item, @NonNull BankListItemHolder holder) {
    Picasso.with(holder.getContext())
      .load(item.getLogoUri(LogoStyle.PRIMARY_24))
      .into(holder.imageView);
    holder.textView.setText(item.getName());
  }
}
