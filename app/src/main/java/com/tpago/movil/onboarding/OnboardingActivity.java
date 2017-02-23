package com.tpago.movil.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;

import com.tpago.movil.app.App;
import com.tpago.movil.R;
import com.tpago.movil.app.BaseActivity;
import com.tpago.movil.app.Fragments;
import com.tpago.movil.graphics.RadialGradientDrawable;
import com.tpago.movil.util.Objects;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class OnboardingActivity extends BaseActivity implements OnboardingScreen {
  private Unbinder unbinder;

  @BindColor(R.color.background_dark_colored_start)
  int backgroundStartColor;
  @BindColor(R.color.background_dark_colored_end)
  int backgroundEndColor;

  @BindView(android.R.id.content)
  View rootView;
  @BindView(R.id.view_placeholder)
  View placeholderView;
  @BindView(R.id.view_container)
  View screenContainerView;
  @BindView(R.id.logo)
  Logo logo;

  private OnboardingComponent component;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Sets the layout of the activity.
    setContentView(R.layout.activity_onboarding);
    // Binds all annotated views and methods.
    unbinder = ButterKnife.bind(this);
    // Initializes the dependency injector.
    component = App.get(this).getAppComponent()
      .plus(new OnboardingModule(this));
    // Adds a listener that gets notified when all the views has been laid out.
    final ViewTreeObserver observer = rootView.getViewTreeObserver();
    if (observer.isAlive()) {
      observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          // Removes the listener that gets notified when all the views has been laid out.
          final ViewTreeObserver observer = rootView.getViewTreeObserver();
          if (Objects.isNotNull(observer)) {
            observer.removeOnGlobalLayoutListener(this);
          }
          // Sets the background of the activity.
          rootView.setBackground(
            new RadialGradientDrawable(
              backgroundStartColor,
              backgroundEndColor,
              rootView.getHeight()));
          // Initializes the application.
          Fragments.replace(getSupportFragmentManager(), InitializationFragment.create());
        }
      });
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Unbinds all annotated views and methods.
    unbinder.unbind();
  }

  @Override
  public OnboardingComponent getOnboardingComponent() {
    return component;
  }
}
