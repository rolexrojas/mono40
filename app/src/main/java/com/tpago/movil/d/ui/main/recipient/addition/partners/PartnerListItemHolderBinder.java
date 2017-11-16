package com.tpago.movil.d.ui.main.recipient.addition.partners;

import android.support.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.tpago.movil.dep.Partner;
import com.tpago.movil.dep.api.ApiImageUriBuilder;
import com.tpago.movil.d.ui.main.list.ListItemHolderBinder;

/**
 * TODO
 *
 * @author hecvasro
 */
class PartnerListItemHolderBinder implements ListItemHolderBinder<Partner, PartnerListItemHolder> {

  @Override
  public void bind(@NonNull Partner item, @NonNull PartnerListItemHolder holder) {
    Picasso.with(holder.getContext())
      .load(ApiImageUriBuilder.build(holder.getContext(), item, ApiImageUriBuilder.Style.PRIMARY_24))
      .into(holder.imageView);
    holder.textView.setText(item.getName());
  }
}
