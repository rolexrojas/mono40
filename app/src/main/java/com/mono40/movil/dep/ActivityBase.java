package com.mono40.movil.dep;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.mono40.movil.R;

import javax.inject.Inject;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


/**
 * @author hecvasro
 */
@Deprecated
public abstract class ActivityBase extends com.mono40.movil.app.ui.activity.base.ActivityBase {

  @Inject BackEventHandler backButtonHandler;

  @LayoutRes
  protected abstract int layoutResourceIdentifier();

  @LayoutRes
  @Override
  protected int layoutResId() {
    return this.layoutResourceIdentifier();
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ViewPump.init(ViewPump.builder()
            .addInterceptor(new CalligraphyInterceptor(
                    new CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Roboto/Roboto-RobotoRegular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()))
            .build());
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
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
