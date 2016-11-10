package com.gbh.movil.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gbh.movil.App;
import com.gbh.movil.R;
import com.gbh.movil.Utils;
import com.gbh.movil.data.MessageHelper;
import com.gbh.movil.ui.BaseActivity;
import com.gbh.movil.ui.UiUtils;
import com.gbh.movil.ui.main.accounts.AccountsFragment;
import com.gbh.movil.ui.main.payments.PaymentsFragment;
import com.gbh.movil.ui.view.widget.SlidingPaneLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class MainActivity extends BaseActivity implements MainScreen {
  private Unbinder unbinder;
  private MainComponent component;

  @Inject
  MessageHelper messageHelper;
  @Inject
  MainPresenter presenter;

  @BindView(R.id.sliding_pane_layout)
  SlidingPaneLayout slidingPaneLayout;
  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @NonNull
  public static Intent getLaunchIntent(@NonNull Context context) {
    return new Intent(context, MainActivity.class);
  }

  private void replaceFragment(@NonNull Fragment fragment, boolean addToBackStack) {
    final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
      .replace(R.id.fragment_container, fragment);
    if (addToBackStack) {
      transaction.addToBackStack(null);
    }
    transaction.commit();
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
    // Sets the startup screen.
    replaceFragment(PaymentsFragment.newInstance(), false);
    // Injects all the annotated dependencies.
    if (Utils.isNull(component)) {
      component = DaggerMainComponent.builder()
        .appComponent(((App) getApplication()).getComponent())
        .build();
    }
    component.inject(this);
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
    // Creates the presenter.
    presenter.create();
  }

  @Override
  protected void onStart() {
    super.onStart();
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
    final SubFragment subFragment;
    switch (view.getId()) {
      case R.id.text_view_payments:
        subFragment = PaymentsFragment.newInstance();
        break;
      case R.id.text_view_accounts:
        subFragment = AccountsFragment.newInstance();
        break;
      case R.id.text_view_add_another_account:
        subFragment = AddAnotherAccountFragment.newInstance();
        break;
      default:
        subFragment = null;
        break;
    }
    if (Utils.isNotNull(subFragment)) {
      setSubScreen(subFragment);
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

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public MainComponent getComponent() {
    return component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSubScreen(@NonNull SubFragment subFragment) {
    final FragmentManager manager = getSupportFragmentManager();
    final Fragment currentFragment = manager.findFragmentById(R.id.fragment_container);
    if (Utils.isNull(currentFragment) || currentFragment.getClass() != subFragment.getClass()) {
      manager.beginTransaction()
        .setCustomAnimations(R.anim.fragment_transition_enter_sibling,
          R.anim.fragment_transition_exit_sibling, R.anim.fragment_transition_enter_sibling,
          R.anim.fragment_transition_exit_sibling)
        .replace(R.id.fragment_container, subFragment)
        .addToBackStack(null)
        .commit();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTitle(@Nullable String title) {
    final ActionBar actionBar = getSupportActionBar();
    if (Utils.isNotNull(actionBar)) {
      actionBar.setTitle(title);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showAccountAdditionOrRemovalNotification(@NonNull String message) {
    UiUtils.createDialog(this, messageHelper.doneWithExclamationMark(),
      message, messageHelper.ok(), null, messageHelper.goToAccounts(),
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          setSubScreen(AccountsFragment.newInstance());
        }
      }).show();
  }
}
