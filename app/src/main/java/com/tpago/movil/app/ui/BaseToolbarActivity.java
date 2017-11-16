package com.tpago.movil.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tpago.movil.R;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author hecvasro
 */
public abstract class BaseToolbarActivity extends BaseActivity {

  @BindView(R.id.toolbar) protected Toolbar toolbar;

  @Inject @HomeButton protected NavButtonClickHandler homeButtonClickHandler;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the toolbar
    this.setSupportActionBar(this.toolbar);

    final ActionBar actionBar = ObjectHelper.checkNotNull(this.getSupportActionBar(), "actionBar");
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        if (!this.homeButtonClickHandler.onNavButtonClicked()) {
          this.finish();
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
