package com.tpago.movil.ui.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class IntroductionFragment extends Fragment {
  private Unbinder unbinder;

  private AutoTabSwitcher autoTabSwitcher;

  @BindView(R.id.view_pager)
  ViewPager viewPager;
  @BindView(R.id.tab_layout)
  TabLayout tabLayout;

  static IntroductionFragment create() {
    return new IntroductionFragment();
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
    // Creates the auto tab switcher.
    autoTabSwitcher = new AutoTabSwitcher(viewPager);
  }

  @Override
  public void onResume() {
    super.onResume();
    // Starts the auto tab switcher.
    autoTabSwitcher.start();
  }

  @Override
  public void onPause() {
    super.onPause();
    // Stops the auto tab switcher.
    autoTabSwitcher.stop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Destroys the auto tab switcher.
    autoTabSwitcher = null;
    // Unbinds all annotated views and methods.
    unbinder.unbind();
  }

  private static final class AutoTabSwitcher extends ViewPager.SimpleOnPageChangeListener {
    private final ViewPager viewPager;

    private Runnable runnable;

    AutoTabSwitcher(ViewPager viewPager) {
      this.viewPager = Preconditions.checkNotNull(viewPager, "viewPager == null");
    }

    private void removeRunnable() {
      if (Objects.isNotNull(runnable)) {
        viewPager.removeCallbacks(runnable);
        runnable = null;
      }
    }

    final void start() {
      viewPager.addOnPageChangeListener(this);
      onPageSelected(viewPager.getCurrentItem());
    }

    final void stop() {
      removeRunnable();
      viewPager.removeOnPageChangeListener(this);
    }

    @Override
    public void onPageSelected(final int position) {
      removeRunnable();
      final int count = viewPager.getAdapter().getCount();
      viewPager.postDelayed(new Runnable(viewPager, (position + 1) % count), 4000L);
    }

    private static final class Runnable implements java.lang.Runnable {
      private final ViewPager viewPager;
      private final int position;

      Runnable(ViewPager viewPager, int position) {
        this.viewPager = Preconditions.checkNotNull(viewPager, "viewPager == null");
        this.position = position;
      }

      @Override
      public void run() {
        viewPager.setCurrentItem(position);
      }
    }
  }
}
