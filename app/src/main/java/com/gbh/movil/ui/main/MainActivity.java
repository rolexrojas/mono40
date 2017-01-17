package com.gbh.movil.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.gbh.movil.App;
import com.gbh.movil.R;
import com.gbh.movil.data.NfcHandler;
import com.gbh.movil.domain.ResetEvent;
import com.gbh.movil.domain.session.SessionManager;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.misc.Utils;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.ui.ActivityModule;
import com.gbh.movil.ui.ChildFragment;
import com.gbh.movil.ui.DialogCreator;
import com.gbh.movil.ui.SwitchableContainerActivity;
import com.gbh.movil.ui.index.IndexActivity;
import com.gbh.movil.ui.main.purchase.PurchaseFragment;
import com.gbh.movil.ui.main.products.ProductsFragment;
import com.gbh.movil.ui.main.payments.PaymentsFragment;
import com.gbh.movil.ui.view.widget.SlidingPaneLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

/**
 * TODO
 *
 * @author hecvasro
 */
public class MainActivity extends SwitchableContainerActivity<MainComponent>
  implements MainContainer, MainScreen {
  private Unbinder unbinder;
  private MainComponent component;

  private Subscription expirationSubscription = Subscriptions.unsubscribed();

  @Inject
  StringHelper stringHelper;
  @Inject
  MainPresenter presenter;
  @Inject
  SessionManager sessionManager;
  @Inject
  EventBus eventBus;
  @Inject
  NfcHandler nfcHandler;

  @BindView(R.id.sliding_pane_layout)
  SlidingPaneLayout slidingPaneLayout;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.text_view_commerce)
  TextView commerceTextView;

  @NonNull
  public static Intent getLaunchIntent(@NonNull Context context) {
    return new Intent(context, MainActivity.class);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    eventBus.dispatch(new ResetEvent());
    return super.dispatchTouchEvent(ev);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    component = DaggerMainComponent.builder()
      .appComponent(((App) getApplication()).getComponent())
      .activityModule(new ActivityModule(this))
      .build();
    component.inject(this);
    // Starts listening session expiration events.
    expirationSubscription = sessionManager.expiration()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Object>() {
        @Override
        public void call(Object notification) {
          final Intent intent = IndexActivity.getLaunchIntent(MainActivity.this);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(intent);
        }
      });
    // Sets the content layout identifier.
    setContentView(R.layout.activity_main);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this);
    // Prepares the action bar.
    setSupportActionBar(toolbar);
    final ActionBar actionBar = getSupportActionBar();
    if (Utils.isNotNull(actionBar)) {
      actionBar.setDisplayShowTitleEnabled(true);
    }
    // Prepares the toolbar.
    toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (slidingPaneLayout.isOpen()) {
          slidingPaneLayout.closePane();
        } else {
          slidingPaneLayout.openPane();
        }
      }
    });
    final boolean enabled = nfcHandler.isAvailable();
    commerceTextView.setEnabled(enabled);
    commerceTextView.setAlpha(enabled ? 1F : 0.3F);
    // Sets the startup screen.
    getSupportFragmentManager().beginTransaction()
      .replace(R.id.container, PaymentsFragment.newInstance())
      .commit();
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
    // Creates the presenter.
    presenter.create();
  }

  @Override
  protected void onStart() {
    super.onStart();
    expirationSubscription = sessionManager.expiration()
      .doOnNext(new Action1<Object>() {
        @Override
        public void call(Object notification) {
          startActivity(IndexActivity.getLaunchIntent(MainActivity.this));
          finish();
        }
      })
      .subscribe();
    // Starts the presenter.
    presenter.start();
  }

  @Override
  protected void onStop() {
    super.onStop();
    // Stops the presenter.
    presenter.stop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Destroys the presenter.
    presenter.destroy();
    // Detaches the screen from the presenter.
    presenter.detachScreen();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
    // Stops listening session expiration events.
    RxUtils.unsubscribe(expirationSubscription);
  }

  /**
   * TODO
   *
   * @param view
   *   TODO
   */
  @OnClick({ R.id.text_view_payments, R.id.text_view_commerce, R.id.text_view_accounts,
    R.id.text_view_profile, R.id.text_view_preferences, R.id.text_view_about, R.id.text_view_help,
    R.id.text_view_add_another_account })
  void onMenuItemButtonClicked(@NonNull View view) {
    if (slidingPaneLayout.isOpen()) {
      slidingPaneLayout.closePane();
    }
    final ChildFragment<MainContainer> childFragment;
    switch (view.getId()) {
      case R.id.text_view_payments:
        childFragment = PaymentsFragment.newInstance();
        break;
      case R.id.text_view_commerce:
        childFragment = PurchaseFragment.newInstance();
        break;
      case R.id.text_view_accounts:
        childFragment = ProductsFragment.newInstance();
        break;
      case R.id.text_view_add_another_account:
        childFragment = AddAnotherProductFragment.newInstance();
        break;
      default:
        childFragment = null;
        break;
    }
    if (Utils.isNotNull(childFragment)) {
      setChildFragment(childFragment, true, true);
    } else {
      DialogCreator.featureNotAvailable(this)
        .show();
    }
  }

  @Override
  public void onBackPressed() {
    if (slidingPaneLayout.isOpen()) {
      slidingPaneLayout.closePane();
    } else {
      super.onBackPressed();
    }
  }

  @Nullable
  @Override
  public MainComponent getComponent() {
    return component;
  }

  @Override
  public void setTitle(@Nullable String title) {
    final ActionBar actionBar = getSupportActionBar();
    if (Utils.isNotNull(actionBar)) {
      actionBar.setTitle(title);
    }
  }

  @Override
  public void openPurchaseScreen() {
    setChildFragment(PurchaseFragment.newInstance(true), true, true);
  }
}
