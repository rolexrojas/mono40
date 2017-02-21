package com.tpago.movil.ui.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;

import com.tpago.movil.App;
import com.tpago.movil.R;
import com.tpago.movil.ui.BaseActivity;
import com.tpago.movil.ui.RadialGradientDrawable;
import com.tpago.movil.util.Objects;

import javax.inject.Inject;

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
  @BindView(R.id.view_screen_container)
  View screenContainerView;
  @BindView(R.id.logo_view)
  LogoView logoView;

  private OnboardingComponent component;

  @Inject
  OnboardingNavigator navigator;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Sets the layout of the activity.
    setContentView(R.layout.activity_onboarding);
    // Binds all annotated views and methods.
    unbinder = ButterKnife.bind(this);
    // Initializes the dependency injector.
    component = DaggerOnboardingComponent.builder()
      .appComponent(((App) getApplication()).getComponent())
      .onboardingModule(new OnboardingModule(this))
      .build();
    // Injects all annotated dependencies.
    component.inject(this);
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
          navigator.startInitialization();
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
  public OnboardingComponent getComponent() {
    return component;
  }
}
