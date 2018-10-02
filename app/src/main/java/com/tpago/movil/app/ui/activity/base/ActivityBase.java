package com.tpago.movil.app.ui.activity.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tpago.movil.app.ui.activity.NavButtonPressEventHandler;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.StringMapper;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author Hector Vasquez
 */
public abstract class ActivityBase extends AppCompatActivity {

  private Unbinder unbinder;

  @Inject protected StringMapper stringMapper;

  @Inject
  protected NavButtonPressEventHandler backButtonPressEventHandler;

  @Inject protected AlertManager alertManager;
  @Inject protected TakeoverLoader takeoverLoader;

  /**
   * Layout resource identifier of the activity
   */
  @LayoutRes
  protected abstract int layoutResId();

  @Override
  protected void attachBaseContext(Context newBase) {
    final Context context = ContextBuilder.create(newBase)
      .function(ContextWrapperLocale::wrap)
      .function(CalligraphyContextWrapper::wrap)
      .build();
    super.attachBaseContext(context);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.setContentView(this.layoutResId());

    // Binds all annotated methods, resources, and views.
    this.unbinder = ButterKnife.bind(this);
  }

  @Override
  protected void onDestroy() {
    // Unbinds all annotated methods, resources, and views.
    this.unbinder.unbind();

    super.onDestroy();
  }

  @Override
  public void onBackPressed() {
    if (!this.backButtonPressEventHandler.accept()) {
      super.onBackPressed();
    }
  }
}
