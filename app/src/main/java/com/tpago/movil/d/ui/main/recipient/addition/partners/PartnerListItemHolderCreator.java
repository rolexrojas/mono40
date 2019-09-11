package com.tpago.movil.d.ui.main.recipient.addition.partners;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.main.list.ListItemHolder;
import com.tpago.movil.d.ui.main.list.ListItemHolderCreator;

/**
 * TODO
 *
 * @author hecvasro
 */
class PartnerListItemHolderCreator implements ListItemHolderCreator<PartnerListItemHolder> {

  private final ListItemHolder.OnClickListener onClickListener;

  /**
   * TODO
   *
   * @param onClickListener TODO
   */
  PartnerListItemHolderCreator(@NonNull ListItemHolder.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  @NonNull
  @Override
  public PartnerListItemHolder create(@NonNull ViewGroup parent) {
    return new PartnerListItemHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.d_list_item_bank, parent, false), onClickListener);
  }
}
