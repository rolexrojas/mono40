package com.tpago.movil.app.ui.main.transaction.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.item.ItemHolder;
import com.tpago.movil.util.ObjectHelper;

import butterknife.BindView;

public final class IndexItemHolder extends ItemHolder {

  public static IndexItemHolder create(ViewGroup parent) {
    ObjectHelper.checkNotNull(parent, "parent");
    final View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.holder_item_main_transaction_index_item, parent, false);
    return new IndexItemHolder(view);
  }

  @BindView(R.id.pictureImageView) ImageView pictureImageView;
  @BindView(R.id.titleTextView) TextView titleTextView;
  @BindView(R.id.subtitleTextView) TextView subtitleTextView;
  @BindView(R.id.actionButton) TextView actionButton;

  private IndexItemHolder(View itemView) {
    super(itemView);
  }
}
