package com.tpago.movil.dep.init.intro;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.dep.init.BaseInitFragment;
import com.tpago.movil.dep.init.LogoAnimator;
import com.tpago.movil.dep.init.PhoneNumberInitFragment;
import com.tpago.movil.dep.widget.AutoTabSwitcher;

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

  @Inject LogoAnimator logoAnimator;
  @Inject @ActivityQualifier FragmentReplacer fragmentReplacer;

  @BindView(R.id.view_pager) ViewPager viewPager;
  @BindView(R.id.tab_layout) TabLayout tabLayout;

  public static IntroFragment create() {
    return new IntroFragment();
  }

  @OnClick(R.id.button_start)
  void onStartButtonClicked() {
    fragmentReplacer.begin(PhoneNumberInitFragment.create())
      .addToBackStack()
      .transition(FragmentReplacer.Transition.SRFO)
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
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Initializes the view pager and the tab layout.
    viewPager.setAdapter(new IntroTabFragmentAdapter(getChildFragmentManager(), getContext()));
    tabLayout.setupWithViewPager(viewPager);
    // Creates the automatic tab switcher.
    autoTabSwitcher = new AutoTabSwitcher(viewPager);
  }

  @Override
  public void onResume() {
    super.onResume();
    // Moves the logo back to the screen.
    logoAnimator.moveTopAndScaleDown();
    // Starts the automatic tab switcher.
    autoTabSwitcher.start();
  }

  @Override
  public void onPause() {
    super.onPause();
    // Stops the automatic tab switcher.
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
