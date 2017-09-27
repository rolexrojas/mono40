package com.tpago.movil.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.tpago.movil.data.StringMapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.SubscriberExceptionEvent;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author Hector Vasquez
 */
public abstract class BaseActivity extends AppCompatActivity {

  private Unbinder unbinder;

  @Inject protected EventBus eventBus;
  @Inject protected StringMapper stringMapper;

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
    setContentView(this.layoutResId());

    // Binds all annotated resources, views and methods.
    this.unbinder = ButterKnife.bind(this);
  }

  @Override
  protected void onStart() {
    super.onStart();

    // Registers the activity to the event bus.
    this.eventBus.register(this);
  }

  @Override
  protected void onStop() {
    // Unregisters the activity from the events bus.
    this.eventBus.unregister(this);

    super.onStop();
  }

  @Override
  protected void onDestroy() {
    // Unbinds all annotated resources, views and methods.
    this.unbinder.unbind();

    super.onDestroy();
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public final void onAlertShowEvent(AlertShowEvent event) {
    final AlertDialog alertDialog = new AlertDialog.Builder(this)
      .setTitle(event.title())
      .setMessage(event.message())
      .setPositiveButton(event.positiveButtonText(), event.positiveButtonListener())
      .setNegativeButton(event.negativeButtonText(), event.negativeButtonListener())
      .create();
    alertDialog.show();
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public final void onSubscriberExceptionEvent(SubscriberExceptionEvent event) {
    Timber.e(
      event.throwable,
      String.format(
        "SubscriberExceptionEvent{causingEvent=%1$s, causingSubscriber=%2$s",
        event.causingEvent,
        event.causingSubscriber
      )
    );

    this.onAlertShowEvent(AlertShowEventHelper.createForUnexpectedFailure(this.stringMapper));
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public final void onTakeoverLoaderShowEvent(TakeoverLoader.ShowEvent event) {
    final FragmentManager fragmentManager = this.getSupportFragmentManager();

    FragmentHelper.dismissByTag(fragmentManager, TakeoverLoader.TAG);

    TakeoverLoader.create()
      .show(fragmentManager, TakeoverLoader.TAG);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public final void onTakeoverLoaderHideEvent(TakeoverLoader.HideEvent event) {
    FragmentHelper.dismissByTag(this.getSupportFragmentManager(), TakeoverLoader.TAG);
  }
}
