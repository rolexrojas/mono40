package com.mono40.movil.d.ui.main.products.transactions;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mono40.movil.R;
import com.mono40.movil.d.ui.view.widget.PrefixableTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO
 *
 * @author hecvasro
 */
class TransactionItemViewHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.text_view_transaction_name)
  TextView nameTextView;
  @BindView(R.id.text_view_transaction_type)
  TextView typeTextView;
  @BindView(R.id.view_amount)
  PrefixableTextView amountTextView;

  TransactionItemViewHolder(View view) {
    super(view);
    // Binds all the annotated views and methods.
    ButterKnife.bind(this, view);
  }
}
