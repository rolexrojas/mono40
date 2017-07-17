package com.tpago.movil.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author hecvasro
 */
public abstract class BaseActivity extends AppCompatActivity {
  private Unbinder unbinder;

  @Inject BackEventHandler backButtonHandler;

  @LayoutRes protected abstract int layoutResourceIdentifier();

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Sets the layout resource identifier of the activity.
    setContentView(layoutResourceIdentifier());
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this);
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

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Unbinds all the annotated resources, views and methods.
    unbinder.unbind();
  }

  @Override
  public void onBackPressed() {
    if (!backButtonHandler.onBackPressed()) {
      super.onBackPressed();
    }
  }
}
