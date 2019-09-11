package com.tpago.movil.d.ui.view.widget;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * {@link LoadIndicator} implementation that uses a {@link
 * androidx.swiperefreshlayout.widget.SwipeRefreshLayout}.
 *
 * @author hecvasro
 */
public class SwipeRefreshLayoutRefreshIndicator implements LoadIndicator {
  private final SwipeRefreshLayout swipeRefreshLayout;

  public SwipeRefreshLayoutRefreshIndicator(@NonNull SwipeRefreshLayout swipeRefreshLayout) {
    this.swipeRefreshLayout = swipeRefreshLayout;
  }

  private void setRefreshing(final boolean refreshing) {
    swipeRefreshLayout.post(new Runnable() {
      @Override
      public void run() {
        swipeRefreshLayout.setRefreshing(refreshing);
      }
    });
  }

  @Override
  public void show() {
    setRefreshing(true);
  }

  @Override
  public void hide() {
    setRefreshing(false);
  }
}
