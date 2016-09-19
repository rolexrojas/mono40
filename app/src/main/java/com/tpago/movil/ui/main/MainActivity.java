package com.tpago.movil.ui.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.tpago.movil.App;
import com.tpago.movil.R;
import com.tpago.movil.ui.SplashFragment;
import com.tpago.movil.ui.view.widget.SlidingPaneLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class MainActivity extends AppCompatActivity implements MainScreen {
  /**
   * TODO
   */
  private static final String FRAGMENT_TAG_SPLASH = "splash";

  private Unbinder unbinder;

  private MainComponent component;

  @Inject
  MainPresenter presenter;

  @BindView(R.id.sliding_pane_layout)
  SlidingPaneLayout slidingPaneLayout;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the dependencies.
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
    toolbar.setNavigationIcon(R.drawable.icon_menu_white_24dp);
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

  @Override
  public void showAccountsScreen() {
    // TODO
  }

  @Override
  public void showMessage(@NonNull String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

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

  @Override
  public void showSplashScreen() {
    final FragmentManager fragmentManager = getSupportFragmentManager();
    if (fragmentManager.findFragmentByTag(FRAGMENT_TAG_SPLASH) == null) {
      fragmentManager.beginTransaction()
        .replace(R.id.frame_layout_full_screen_container, new SplashFragment(),
          FRAGMENT_TAG_SPLASH)
        .commit();
    }
  }

  @Override
  public void hideSplashScreen() {
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG_SPLASH);
    if (fragment != null) {
      fragmentManager.beginTransaction()
        .remove(fragment)
        .commit();
    }
  }
}
