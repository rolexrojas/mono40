package com.tpago.movil.ui.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.tpago.movil.R;
import com.tpago.movil.ui.BaseActivity;
import com.tpago.movil.ui.RadialGradientDrawable;
import com.tpago.movil.ui.onboarding.intro.IntroFragment;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class OnboardingActivity extends BaseActivity {
  private Unbinder unbinder;

  @BindColor(R.color.background_dark_colored_start)
  int backgroundStartColor;
  @BindColor(R.color.background_dark_colored_end)
  int backgroundEndColor;

  @BindView(android.R.id.content)
  View rootView;
  @BindView(R.id.frame_layout_logo)
  FrameLayout logoFrameLayout;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Sets the layout of the activity.
    setContentView(R.layout.activity_onboarding);
    // Binds all annotated views and methods.
    unbinder = ButterKnife.bind(this);
    // Sets the background of the activity.
    RadialGradientDrawable.createAndSet(rootView, backgroundStartColor, backgroundEndColor);

    getSupportFragmentManager().beginTransaction()
      .replace(R.id.frame_layout_container, IntroFragment.create())
      .commit();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Unbinds all annotated views and methods.
    unbinder.unbind();
  }
}
