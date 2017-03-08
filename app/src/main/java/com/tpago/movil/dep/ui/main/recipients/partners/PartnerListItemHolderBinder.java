package com.tpago.movil.dep.ui.main.recipients.partners;

import android.support.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.tpago.movil.Partner;
import com.tpago.movil.dep.data.res.DepAssetProvider;
import com.tpago.movil.dep.ui.main.list.ListItemHolderBinder;

/**
 * TODO
 *
 * @author hecvasro
 */
class PartnerListItemHolderBinder implements ListItemHolderBinder<Partner, PartnerListItemHolder> {
  private final DepAssetProvider assetProvider;

  PartnerListItemHolderBinder(DepAssetProvider assetProvider) {
    this.assetProvider = assetProvider;
  }

  @Override
  public void bind(@NonNull Partner item, @NonNull PartnerListItemHolder holder) {
    Picasso.with(holder.getContext())
      .load(assetProvider.getLogoUri(item, DepAssetProvider.STYLE_24_PRIMARY))
      .into(holder.imageView);
    holder.textView.setText(item.getName());
  }
}
