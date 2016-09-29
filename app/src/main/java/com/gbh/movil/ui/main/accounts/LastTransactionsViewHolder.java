package com.gbh.movil.ui.main.accounts;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.gbh.movil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hecvasro
 */
class LastTransactionsViewHolder extends RecyclerView.ViewHolder implements
  View.OnClickListener {
  /**
   * TODO
   */
  @BindView(R.id.button)
  Button button;

  /**
   * TODO
   *
   * @param view
   *   TODO
   */
  LastTransactionsViewHolder(View view) {
    super(view);
    // Binds all the annotated views and methods.
    ButterKnife.bind(this, view);
    // Adds a listener that gets notified every time the button is clicked.
    button.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view == button) {
      // TODO: Open the last transactions screen.
    }
  }
}
