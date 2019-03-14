package com.tpago.movil.app.ui.activity.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.tpago.movil.app.ui.activity.NavButtonPressEventHandler;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.dep.init.InitActivityBase;
import com.tpago.movil.util.LogoutTimerService;

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
  private LocalBroadcastManager localBroadcastManager;
  private LogoutReceiver logoutReceiver;

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
    localBroadcastManager = LocalBroadcastManager.getInstance(this);
    logoutReceiver = new LogoutReceiver();

    this.setContentView(this.layoutResId());

    // Binds all annotated methods, resources, and views.
    this.unbinder = ButterKnife.bind(this);
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

  class LogoutReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      ActivityBase.this.startActivity(InitActivityBase.getLaunchIntent(ActivityBase.this));
      ActivityBase.this.finish();
    }
  }
}
