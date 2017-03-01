package com.tpago.movil.init.onboarding;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tpago.movil.R;

/**
 * @author hecvasro
 */
final class OnboardingTabFragmentAdapter extends FragmentPagerAdapter {
  private static final int COUNT = 4;

  private final int[] titles;
  private final int[] descriptions;

  OnboardingTabFragmentAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
    titles = new int[] {
      R.string.onboarding_first_label_title,
      R.string.onboarding_second_label_title,
      R.string.onboarding_third_label_title,
      R.string.onboarding_fourth_label_title
    };
    descriptions = new int[] {
      R.string.onboarding_first_label_description,
      R.string.onboarding_second_label_description,
      R.string.onboarding_third_label_description,
      R.string.onboarding_fourth_label_description
    };
  }

  @Override
  public Fragment getItem(int position) {
    return OnboardingTabFragment.create(titles[position], descriptions[position]);
  }

  @Override
  public int getCount() {
    return COUNT;
  }
}
