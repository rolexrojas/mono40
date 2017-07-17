package com.tpago.movil.d.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tpago.movil.app.App;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class BaseActivity extends AppCompatActivity {
  @LayoutRes protected abstract int layoutResourceIdentifier();

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layoutResourceIdentifier());
  }

  @Override
  protected void onResume() {
    super.onResume();
    App.get(this).setVisible(true);
  }

  @Override
  protected void onPause() {
    super.onPause();
    App.get(this).setVisible(false);
  }
}
