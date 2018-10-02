package com.tpago.movil.app.ui.activity.toolbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.activity.base.ActivityBase;
import com.tpago.movil.util.ObjectHelper;

import butterknife.BindView;

/**
 * @author hecvasro
 */
public abstract class ActivityToolbarBase extends ActivityBase {

  @BindView(R.id.toolbar) protected Toolbar toolbar;

  protected ToolbarManager toolbarManager;

  public final ToolbarManager toolbarManager() {
    return this.toolbarManager;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the toolbar.
    this.toolbar.setTitle(""); // Required, if not set, Toolbar#setTitle won't work later.
    this.toolbar.setSubtitle(""); // Required, if not set, Toolbar#setProductTypeAndNumber won't work later.

    this.setSupportActionBar(this.toolbar);

    final ActionBar actionBar = ObjectHelper
      .checkNotNull(this.getSupportActionBar(), "actionBar");
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowTitleEnabled(true);

    // Instantiates the toolbar manager.
    this.toolbarManager = ToolbarManager.create(this.toolbar);
  }

  @Override
  protected void onStart() {
    super.onStart();

    // Initializes the toolbar manager.
    this.toolbarManager
      .setHomeButtonDrawable(R.drawable.ic_arrow_back_white_24dp)
      .setHomeButtonAction(this::onBackPressed);
  }
}
