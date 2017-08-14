package com.tpago.movil.d.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.tpago.movil.Session;
import com.tpago.movil.TimeOutManager;
import com.tpago.movil.app.App;
import com.tpago.movil.R;
import com.tpago.movil.d.domain.ResetEvent;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.misc.Utils;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.ui.DepActivityModule;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.SwitchableContainerActivity;
import com.tpago.movil.d.ui.main.purchase.PurchaseFragment;
import com.tpago.movil.d.ui.main.products.ProductsFragment;
import com.tpago.movil.d.ui.main.payments.PaymentsFragment;
import com.tpago.movil.d.ui.view.widget.SlidingPaneLayout;
import com.tpago.movil.init.InitActivity;
import com.tpago.movil.main.MainModule;
import com.tpago.movil.main.purchase.NonNfcPurchaseFragment;
import com.tpago.movil.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
@Deprecated
public class DepMainActivity
  extends SwitchableContainerActivity<DepMainComponent>
  implements MainContainer,
  MainScreen,
  TimeOutManager.TimeOutHandler {
  private static final String KEY_SESSION = "session";

  private Unbinder unbinder;
  private DepMainComponent component;
  private OnBackPressedListener onBackPressedListener;

  private boolean shouldRequestAuthentication = false;

  @Inject TimeOutManager timeOutManager;

  @Inject
  StringHelper stringHelper;
  @Inject
  MainPresenter presenter;
  @Inject
  SessionManager sessionManager;
  @Inject
  EventBus eventBus;
  @Inject
  PosBridge posBridge;

  @BindView(R.id.sliding_pane_layout)
  SlidingPaneLayout slidingPaneLayout;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.linear_layout_delete)
  LinearLayout deleteLinearLayout;
  @BindView(R.id.image_button_cancel)
  ImageButton cancelImageButton;
  @BindView(R.id.image_button_delete)
  ImageButton deleteImageButton;

  @NonNull
  public static Intent getLaunchIntent(
    Context context,
    Session session
  ) {
    final Intent i = new Intent(context, DepMainActivity.class);
    i.putExtra(KEY_SESSION, session);
    return i;
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    eventBus.dispatch(new ResetEvent());
    return super.dispatchTouchEvent(ev);
  }

  @Override
  protected int layoutResourceIdentifier() {
    return R.layout.d_activity_main;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    unbinder = ButterKnife.bind(this);
    // Injects all the annotated dependencies.
    component = DaggerDepMainComponent.builder()
      .appComponent(((App) getApplication()).getComponent())
      .mainModule(new MainModule(((Session) getIntent().getParcelableExtra(KEY_SESSION)), this))
      .depActivityModule(new DepActivityModule(this))
      .build();
    component.inject(this);
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
    // Sets the startup screen.
    setChildFragment(PaymentsFragment.newInstance(), false, false);
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
    // Creates the presenter.
    presenter.create();

    timeOutManager.start();
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (shouldRequestAuthentication) {
      startActivity(InitActivity.getLaunchIntent(this));
      finish();
    } else {
      presenter.start();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onStop() {
    super.onStop();
    presenter.stop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    timeOutManager.stop();

    // Destroys the presenter.
    presenter.destroy();
    // Detaches the screen from the presenter.
    presenter.detachScreen();

    unbinder.unbind();
  }

  private void setChildFragment(ChildFragment<MainContainer> childFragment) {
    this.setChildFragment(childFragment, true, true);
  }

  @OnClick({
    R.id.main_menuItem_pay,
    R.id.main_menuItem_purchase,
    R.id.main_menuItem_transfer,
    R.id.main_menuItem_recharge,
    R.id.main_menuItem_disburse,
    R.id.main_menuItem_wallet,
    R.id.main_menuItem_settings,
    R.id.main_menuItem_exit
  })
  final void onMenuItemButtonClicked(@NonNull View view) {
    if (this.slidingPaneLayout.isOpen()) {
      this.slidingPaneLayout.closePane();
    }

    switch (view.getId()) {
      case R.id.main_menuItem_pay:
        this.setChildFragment(PaymentsFragment.newInstance());
        break;
      case R.id.main_menuItem_purchase:
        if (this.posBridge.checkIfUsable()) {
          this.setChildFragment(PurchaseFragment.newInstance());
        } else {
          this.setChildFragment(NonNfcPurchaseFragment.create());
        }
        break;
      case R.id.main_menuItem_transfer:
        Dialogs.featureNotAvailable(this)
          .show();
        break;
      case R.id.main_menuItem_recharge:
        Dialogs.featureNotAvailable(this)
          .show();
        break;
      case R.id.main_menuItem_disburse:
        Dialogs.featureNotAvailable(this)
          .show();
        break;
      case R.id.main_menuItem_wallet:
        this.setChildFragment(ProductsFragment.newInstance());
        break;
      case R.id.main_menuItem_settings:
        Dialogs.featureNotAvailable(this)
          .show();
        break;
      case R.id.main_menuItem_exit:
        break;
    }
  }

  @Override
  public void onBackPressed() {
    if (slidingPaneLayout.isOpen()) {
      slidingPaneLayout.closePane();
    } else if (Objects.checkIfNull(onBackPressedListener) || !onBackPressedListener.onBackPressed()) {
      super.onBackPressed();
    }
  }

  @Nullable
  @Override
  public DepMainComponent getComponent() {
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
    final ChildFragment<MainContainer> childFragment;
    if (posBridge.checkIfUsable()) {
      childFragment = PurchaseFragment.newInstance(true);
    } else {
      childFragment = NonNfcPurchaseFragment.create();
    }
    setChildFragment(childFragment, true, true);
  }

  public void showDeleteLinearLayout() {
    deleteLinearLayout.setVisibility(View.VISIBLE);
    toolbar.setVisibility(View.INVISIBLE);
    toolbar.setEnabled(false);
  }

  public void setOnCancelButtonClickedListener(View.OnClickListener listener) {
    cancelImageButton.setOnClickListener(listener);
  }

  public void setDeleteButtonEnabled(boolean enabled) {
    deleteImageButton.setEnabled(enabled);
    deleteImageButton.setAlpha(enabled ? 1F : 0.5F);
  }

  public void setOnDeleteButtonClickListener(View.OnClickListener listener) {
    deleteImageButton.setOnClickListener(listener);
  }

  public void hideDeleteLinearLayout() {
    toolbar.setEnabled(true);
    toolbar.setVisibility(View.VISIBLE);
    deleteLinearLayout.setVisibility(View.GONE);
  }

  public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
    this.onBackPressedListener = onBackPressedListener;
  }

  @Override
  public void onUserInteraction() {
    super.onUserInteraction();
    timeOutManager.reset();
  }

  @Override
  public void handleTimeOut() {
    if (App.get(this).isVisible()) {
      startActivity(InitActivity.getLaunchIntent(this));
      finish();
    } else {
      shouldRequestAuthentication = true;
    }
  }

  public interface OnBackPressedListener {
    boolean onBackPressed();
  }
}
