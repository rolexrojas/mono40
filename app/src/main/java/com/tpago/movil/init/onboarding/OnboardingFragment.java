package com.tpago.movil.init.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.app.ActivityQualifier;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.init.BaseInitFragment;
import com.tpago.movil.init.InitFragment;
import com.tpago.movil.widget.AutoTabSwitcher;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class OnboardingFragment extends BaseInitFragment {
  private Unbinder unbinder;
  private AutoTabSwitcher autoTabSwitcher;

  @Inject @ActivityQualifier FragmentReplacer fragmentReplacer;

  @BindView(R.id.tab_layout) TabLayout tabLayout;
  @BindView(R.id.view_pager) ViewPager viewPager;

  public static OnboardingFragment create() {
    return new OnboardingFragment();
  }

  @OnClick(R.id.button_skip)
  void onSkipButtonClicked() {
    fragmentReplacer.begin(InitFragment.create())
      .commit();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all annotated dependencies.
    getInitComponent().inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_onboarding, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Initializes the view pager and the tab layout.
    viewPager.setAdapter(new OnboardingTabFragmentAdapter(getChildFragmentManager()));
    tabLayout.setupWithViewPager(viewPager);
    // Creates the automatic tab switcher.
    autoTabSwitcher = new AutoTabSwitcher(viewPager);
  }

  @Override
  public void onResume() {
    super.onResume();
    autoTabSwitcher.start();
  }

  @Override
  public void onPause() {
    super.onPause();
    autoTabSwitcher.stop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Destroys the auto tab switcher.
    autoTabSwitcher = null;
    // Unbinds all the annotated resources, views and method.
    unbinder.unbind();
  }
}
