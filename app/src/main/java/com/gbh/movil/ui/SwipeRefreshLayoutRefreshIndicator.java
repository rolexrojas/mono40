package com.gbh.movil.ui;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * {@link RefreshIndicator} implementation that uses a {@link
 * android.support.v4.widget.SwipeRefreshLayout}.
 *
 * @author hecvasro
 */
public class SwipeRefreshLayoutRefreshIndicator implements RefreshIndicator {
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
