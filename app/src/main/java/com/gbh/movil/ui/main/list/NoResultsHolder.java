package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.gbh.movil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO
 *
 * @author hecvasro
 */
public class NoResultsHolder extends Holder {
  @BindView(R.id.no_results_query)
  TextView queryTextView;

  /**
   * {@inheritDoc}
   */
  public NoResultsHolder(@NonNull View rootView) {
    super(rootView);
    ButterKnife.bind(this, this.rootView);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final TextView getQueryTextView() {
    return queryTextView;
  }
}
