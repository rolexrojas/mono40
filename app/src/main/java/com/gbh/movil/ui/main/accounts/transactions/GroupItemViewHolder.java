package com.gbh.movil.ui.main.accounts.transactions;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * TODO
 * @author hecvasro
 */
class GroupItemViewHolder extends RecyclerView.ViewHolder {
  GroupItemViewHolder(View view) {
    super(view);
  }

  @NonNull
  TextView getTextView() {
    return (TextView) itemView;
  }
}
