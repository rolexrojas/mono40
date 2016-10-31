package com.gbh.movil.ui.main.accounts.transactions;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gbh.movil.R;
import com.gbh.movil.ui.view.widget.AmountView;

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
  AmountView amountView;

  TransactionItemViewHolder(View view) {
    super(view);
    // Binds all the annotated views and methods.
    ButterKnife.bind(this, view);
  }
}
