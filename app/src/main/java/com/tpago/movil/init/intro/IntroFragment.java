package com.tpago.movil.init.intro;

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
import com.tpago.movil.init.register.RegisterFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class IntroFragment extends BaseInitFragment {
  private Unbinder unbinder;

  private AutoTabSwitcher autoTabSwitcher;

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  @BindView(R.id.view_pager)
  ViewPager viewPager;
  @BindView(R.id.tab_layout)
  TabLayout tabLayout;

  public static IntroFragment create() {
    return new IntroFragment();
  }

  @OnClick(R.id.button_start)
  void onStartButtonClicked() {
    fragmentReplacer.begin(RegisterFragment.create())
      .addToBackStack()
      .setTransition(FragmentReplacer.Transition.SRFO)
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
    return inflater.inflate(R.layout.fragment_intro, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Initializes the view pager and the tab layout.
    viewPager.setAdapter(IntroTabFragmentAdapter.create(getChildFragmentManager()));
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
