package com.tpago.movil.app.ui.main.transaction.item;

import com.squareup.picasso.Picasso;
import com.tpago.movil.app.ui.item.ItemHolderBinder;

public final class IndexItemHolderBinder implements ItemHolderBinder<IndexItem, IndexItemHolder> {

  public static IndexItemHolderBinder create() {
    return new IndexItemHolderBinder();
  }

  private IndexItemHolderBinder() {
  }

  @Override
  public void bind(IndexItem item, IndexItemHolder holder) {
    Picasso.with(holder.context)
      .load(item.pictureUri())
      .noFade()
      .into(holder.pictureImageView);

    holder.titleTextView.setText(item.titleText());
    holder.subtitleTextView.setText(item.subtitleText());

    holder.actionButton.setText(item.actionText());
    holder.actionButton.setOnClickListener((view) -> item.onRunAction());
  }
}
