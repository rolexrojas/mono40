package com.mono40.movil.d.ui.main.products.transactions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
