package com.tpago.movil.app;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author hecvasro
 */
public abstract class BaseActivity extends AppCompatActivity {
  @Inject
  BackEventHandler backButtonHandler;

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override
  public void onBackPressed() {
    if (!backButtonHandler.onBackPressed()) {
      super.onBackPressed();
    }
  }
}
