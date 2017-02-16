package com.tpago.movil.ui.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tpago.movil.R;
import com.tpago.movil.ui.BaseActivity;
import com.tpago.movil.ui.RadialGradientDrawable;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class IndexActivity extends BaseActivity {
  private Unbinder unbinder;

  @BindColor(R.color.background_dark_colored_start)
  int backgroundStartColor;
  @BindColor(R.color.background_dark_colored_end)
  int backgroundEndColor;

  @BindView(android.R.id.content)
  View rootView;
  @BindView(R.id.animated_logo)
  AnimatedLogo animatedLogo;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Sets the layout of the activity.
    setContentView(R.layout.activity_index);
    // Binds all annotated views and methods.
    unbinder = ButterKnife.bind(this);
    // Sets the background of the activity.
    RadialGradientDrawable.createAndSet(rootView, backgroundStartColor, backgroundEndColor);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Unbinds all annotated views and methods.
    unbinder.unbind();
  }

  @OnClick(android.R.id.content)
  void onContentClicked() {
    animatedLogo.toggle();
  }
}
