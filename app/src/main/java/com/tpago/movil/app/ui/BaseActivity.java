package com.tpago.movil.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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

  @Inject protected StringMapper stringMapper;
  @Inject protected AlertManager alertManager;
  @Inject @BackButton protected NavButtonClickHandler backButtonClickHandler;

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

    // Sets the layout of the activity.
    this.setContentView(this.layoutResId());

    // Binds all annotated resources, views and methods.
    this.unbinder = ButterKnife.bind(this);
  }

  @Override
  protected void onDestroy() {
    // Unbinds all annotated resources, views and methods.
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
