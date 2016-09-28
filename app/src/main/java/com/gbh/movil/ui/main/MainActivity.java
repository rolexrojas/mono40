package com.gbh.movil.ui.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.gbh.movil.App;
import com.gbh.movil.R;
import com.gbh.movil.ui.BaseActivity;
import com.gbh.movil.ui.SplashFragment;
import com.gbh.movil.ui.main.accounts.AccountsFragment;
import com.gbh.movil.ui.main.payments.PaymentsFragment;
import com.gbh.movil.ui.main.pin.PinConfirmationFragment;
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
public class MainActivity extends BaseActivity implements ParentScreen, MainScreen {
  /**
   * TODO
   */
  private static final String FRAGMENT_TAG_SPLASH = "splash";

  /**
   * TODO
   */
  private Unbinder unbinder;

  /**
   * TODO
   */
  private MainComponent component;

  @Inject
  MainPresenter presenter;

  @BindView(R.id.sliding_pane_layout)
  SlidingPaneLayout slidingPaneLayout;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  /**
   * TODO
   *
   * @param fragment
   *   TODO
   * @param addToBackStack
   *   TODO
   */
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
    // Injects all the annotated dependencies.
    if (component == null) {
      component = DaggerMainComponent.builder()
        .appComponent(((App) getApplication()).getComponent())
        .mainModule(new MainModule(this))
        .build();
    }
    component.inject(this);
    // Sets the content layout identifier.
    setContentView(R.layout.activity_main);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this);
    // Prepares the action bar.
    setSupportActionBar(toolbar);
    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
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
  }

  @Override
  protected void onStart() {
    super.onStart();
    presenter.start();
  }

  @Override
  protected void onStop() {
    super.onStop();
    presenter.stop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
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
    R.id.button_add_another_account })
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
      default:
        subFragment = null;
        break;
    }
    if (subFragment != null) {
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
    final Fragment currentSubFragment = manager.findFragmentById(R.id.fragment_container);
    if (currentSubFragment == null || currentSubFragment.getClass() != subFragment.getClass()) {
      manager.beginTransaction()
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
    if (actionBar != null) {
      actionBar.setTitle(title);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showAccountsScreen() {
    setSubScreen(AccountsFragment.newInstance());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showMessage(@NonNull String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showDialog(@NonNull String title, @NonNull String description,
    @NonNull String positiveOptionText,
    @Nullable final OnOptionClickedListener positiveOptionListener,
    @Nullable String neutralOptionText,
    @Nullable final OnOptionClickedListener neutralOptionListener) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(this)
      .setCancelable(false)
      .setTitle(title)
      .setMessage(description)
      .setPositiveButton(positiveOptionText, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          if (positiveOptionListener != null) {
            positiveOptionListener.onClick();
          }
        }
      });
    if (neutralOptionText != null) {
      builder.setNegativeButton(neutralOptionText, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          if (neutralOptionListener != null) {
            neutralOptionListener.onClick();
          }
        }
      });
    }
    builder.show();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showSplashScreen() {
    final FragmentManager fragmentManager = getSupportFragmentManager();
    if (fragmentManager.findFragmentByTag(FRAGMENT_TAG_SPLASH) == null) {
      fragmentManager.beginTransaction()
        .replace(R.id.fragment_container_full_screen, SplashFragment.newInstance(),
          FRAGMENT_TAG_SPLASH)
        .commit();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void hideSplashScreen() {
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG_SPLASH);
    if (fragment != null) {
      fragmentManager.beginTransaction()
        .remove(fragment)
        .commit();
    }
    final DialogFragment dialogFragment = new PinConfirmationFragment();
    dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.FullScreenDialogTheme);
    dialogFragment.show(fragmentManager, null);
  }
}
