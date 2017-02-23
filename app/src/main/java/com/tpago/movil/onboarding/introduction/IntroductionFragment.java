package com.tpago.movil.onboarding.introduction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.app.Fragments;
import com.tpago.movil.onboarding.OnboardingFragment;
import com.tpago.movil.onboarding.registration.RegistrationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class IntroductionFragment extends OnboardingFragment {
  private Unbinder unbinder;

  private AutoTabSwitcher autoTabSwitcher;

  @BindView(R.id.view_pager)
  ViewPager viewPager;
  @BindView(R.id.tab_layout)
  TabLayout tabLayout;

  public static IntroductionFragment create() {
    return new IntroductionFragment();
  }

  @OnClick(R.id.button_start)
  void onStartButtonClicked() {
    Fragments.replace(
      getFragmentManager(),
      RegistrationFragment.create(),
      Fragments.Transition.SRFO,
      true);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_introduction, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Initializes the view pager and the tab layout.
    viewPager.setAdapter(IntroductionTabFragmentAdapter.create(getChildFragmentManager()));
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
    autoTabSwitcher = null;
    unbinder.unbind();
  }
}
