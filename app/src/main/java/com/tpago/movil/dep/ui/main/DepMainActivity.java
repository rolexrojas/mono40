package com.tpago.movil.dep.ui.main;

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
import android.widget.TextView;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.Session;
import com.tpago.movil.TimeOutManager;
import com.tpago.movil.app.App;
import com.tpago.movil.R;
import com.tpago.movil.dep.data.NfcHandler;
import com.tpago.movil.dep.domain.ResetEvent;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.domain.util.EventBus;
import com.tpago.movil.dep.misc.Utils;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.ui.DepActivityModule;
import com.tpago.movil.dep.ui.ChildFragment;
import com.tpago.movil.dep.ui.Dialogs;
import com.tpago.movil.dep.ui.SwitchableContainerActivity;
import com.tpago.movil.dep.ui.main.purchase.PurchaseFragment;
import com.tpago.movil.dep.ui.main.products.ProductsFragment;
import com.tpago.movil.dep.ui.main.payments.PaymentsFragment;
import com.tpago.movil.dep.ui.view.widget.SlidingPaneLayout;
import com.tpago.movil.init.InitActivity;
import com.tpago.movil.main.MainModule;
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
  NfcHandler nfcHandler;

  @BindView(R.id.sliding_pane_layout)
  SlidingPaneLayout slidingPaneLayout;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.text_view_commerce)
  TextView commerceTextView;
  @BindView(R.id.linear_layout_delete)
  LinearLayout deleteLinearLayout;
  @BindView(R.id.image_button_cancel)
  ImageButton cancelImageButton;
  @BindView(R.id.image_button_delete)
  ImageButton deleteImageButton;

  @NonNull
  public static Intent getLaunchIntent(
    Context context,
    Session session) {
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
    return R.layout.activity_main;
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
    final boolean enabled = nfcHandler.isAvailable();
    commerceTextView.setEnabled(enabled);
    commerceTextView.setAlpha(enabled ? 1F : 0.3F);
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
    // Starts the presenter.
    presenter.start();
  }

  @Override
  protected void onResume() {
    super.onResume();
    ButterKnife.<TextView>findById(slidingPaneLayout, R.id.text_view_add_another_account)
      .setText(String.format("%1$s (%2$s)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
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
    timeOutManager.stop();

    // Destroys the presenter.
    presenter.destroy();
    // Detaches the screen from the presenter.
    presenter.detachScreen();

    unbinder.unbind();
  }

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
      default:
        childFragment = null;
        break;
    }
    if (Utils.isNotNull(childFragment)) {
      setChildFragment(childFragment, true, true);
    } else {
      Dialogs.featureNotAvailable(this)
        .show();
    }
  }

  @Override
  public void onBackPressed() {
    if (slidingPaneLayout.isOpen()) {
      slidingPaneLayout.closePane();
    } else if (Objects.isNull(onBackPressedListener) || !onBackPressedListener.onBackPressed()) {
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
    setChildFragment(PurchaseFragment.newInstance(true), true, true);
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
    startActivity(InitActivity.getLaunchIntent(this));
    finish();
  }

  public interface OnBackPressedListener {
    boolean onBackPressed();
  }
}
