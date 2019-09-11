package com.tpago.movil.app.ui.item;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * @author hecvasro
 */
public abstract class ItemHolder extends RecyclerView.ViewHolder {

  public final Context context;

  protected ItemHolder(View itemView) {
    super(itemView);

    this.context = this.itemView.getContext();

    ButterKnife.bind(this, this.itemView);
  }
}
