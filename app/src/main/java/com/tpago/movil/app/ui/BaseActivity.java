package com.tpago.movil.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tpago.movil.app.App;
import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.data.StringMapper;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author Hector Vasquez
 */
public abstract class BaseActivity extends AppCompatActivity {

  private Unbinder unbinder;

  protected ComponentBuilderSupplier parentComponentBuilderSupplier;

  @Inject @BackButton protected NavButtonClickHandler backButtonClickHandler;
  @Inject protected AlertManager alertManager;
  @Inject protected StringMapper stringMapper;
  @Inject protected TakeoverLoader takeoverLoader;

  /**
   * Layout resource identifier of the activity
   */
  @LayoutRes
  protected abstract int layoutResId();

  @Override
  protected void attachBaseContext(Context newBase) {
    final Context context = ContextBuilder.create(newBase)
      .wrapperFunction(LocaleContextWrapper::wrap)
      .wrapperFunction(CalligraphyContextWrapper::wrap)
      .build();

    super.attachBaseContext(context);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.setContentView(this.layoutResId());

    this.unbinder = ButterKnife.bind(this);

    this.parentComponentBuilderSupplier = App.get(this)
      .componentBuilderSupplier();
  }

  @Override
  protected void onDestroy() {
    this.parentComponentBuilderSupplier = null;

    this.unbinder.unbind();

    super.onDestroy();
  }

  @Override
  public void onBackPressed() {
    if (!this.backButtonClickHandler.onNavButtonClicked()) {
      super.onBackPressed();
    }
  }
}
