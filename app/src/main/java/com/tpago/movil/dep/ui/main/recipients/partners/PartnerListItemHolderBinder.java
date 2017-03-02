package com.tpago.movil.dep.ui.main.recipients.partners;

import android.support.annotation.NonNull;

import com.tpago.movil.Partner;
import com.tpago.movil.dep.ui.main.list.ListItemHolderBinder;

/**
 * TODO
 *
 * @author hecvasro
 */
class PartnerListItemHolderBinder implements ListItemHolderBinder<Partner, PartnerListItemHolder> {
  @Override
  public void bind(@NonNull Partner item, @NonNull PartnerListItemHolder holder) {
    // TODO: Load partner's picture.
    holder.textView.setText(item.getName());
  }
}
