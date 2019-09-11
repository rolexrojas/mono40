package com.tpago.movil.app.ui.activity.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.activity.NavButtonPressEventHandler;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.d.misc.Utils;
import com.tpago.movil.dep.init.InitActivityBase;
import com.tpago.movil.util.LogoutTimerService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

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
  private LocalBroadcastManager localBroadcastManager;
  private LogoutReceiver logoutReceiver;

  /**
   * Layout resource identifier of the activity
   */
  @LayoutRes
  protected abstract int layoutResId();

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ViewPump.init(ViewPump.builder()
            .addInterceptor(new CalligraphyInterceptor(
                    new CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()))
            .build());
    localBroadcastManager = LocalBroadcastManager.getInstance(this);
    logoutReceiver = new LogoutReceiver();

    this.setContentView(this.layoutResId());

    // Binds all annotated methods, resources, and views.
    this.unbinder = ButterKnife.bind(this);
  }

  @Override
  protected void onResume() {
    super.onResume();

    final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
            .findViewById(android.R.id.content)).getChildAt(0);
    setupUI(viewGroup);
  }

  @Override
  protected void onStart() {
    super.onStart();
    IntentFilter intentFilter = new IntentFilter(LogoutTimerService.LOGOUT_BROADCAST);
    localBroadcastManager.registerReceiver(logoutReceiver, intentFilter);
  }

  @Override
  protected void onPause() {
    super.onPause();
    localBroadcastManager.unregisterReceiver(logoutReceiver);
  }

  @Override
  public void onUserInteraction() {
    super.onUserInteraction();
    localBroadcastManager.sendBroadcast(new Intent(LogoutTimerService.USER_INTERACTION_BROADCAST));
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

  public void setupUI(View view) {

    // Set up touch listener for non-text box views to hide keyboard.
    if (!(view instanceof EditText)) {
      view.setOnTouchListener(new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
          Utils.hideSoftKeyboard(ActivityBase.this);
          return false;
        }
      });
    }

    //If a layout container, iterate over children and seed recursion.
    if (view instanceof ViewGroup) {
      for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
        View innerView = ((ViewGroup) view).getChildAt(i);
        setupUI(innerView);
      }
    }
  }

  class LogoutReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      ActivityBase.this.startActivity(InitActivityBase.getLaunchIntent(ActivityBase.this));
      ActivityBase.this.finish();
    }
  }
}
