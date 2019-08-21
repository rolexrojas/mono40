package com.tpago.movil.dep;

import android.content.Context;
import androidx.annotation.LayoutRes;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class ActivityBase extends com.tpago.movil.app.ui.activity.base.ActivityBase {

  @Inject BackEventHandler backButtonHandler;

  @LayoutRes
  protected abstract int layoutResourceIdentifier();

  @LayoutRes
  @Override
  protected int layoutResId() {
    return this.layoutResourceIdentifier();
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override
  protected void onResume() {
    super.onResume();
    App.get(this)
      .setVisible(true);
  }

  @Override
  protected void onPause() {
    super.onPause();
    App.get(this)
      .setVisible(false);
  }

  @Override
  public void onBackPressed() {
    if (!backButtonHandler.onBackPressed()) {
      super.onBackPressed();
    }
  }
}
