package com.tpago.movil.ui.onboarding.introduction;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tpago.movil.R;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
final class IntroductionTabFragmentAdapter extends FragmentPagerAdapter {
  private static final int COUNT = 3;

  private final int[] arts;
  private final int[] titles;
  private final int[] descriptions;

  static IntroductionTabFragmentAdapter create(FragmentManager fragmentManager) {
    return new IntroductionTabFragmentAdapter(
      Preconditions.checkNotNull(fragmentManager, "fragmentManager == null"));
  }

  private IntroductionTabFragmentAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
    arts = new int[COUNT];
    titles = new int[] {
      R.string.introduction_tab_title_welcome,
      R.string.introduction_tab_title_purchase,
      R.string.introduction_tab_title_money
    };
    descriptions = new int[] {
      R.string.introduction_tab_description_welcome,
      R.string.introduction_tab_description_purchase,
      R.string.introduction_tab_description_money
    };
  }

  @Override
  public Fragment getItem(int position) {
    return IntroductionTabFragment.create(arts[position], titles[position], descriptions[position]);
  }

  @Override
  public int getCount() {
    return COUNT;
  }
}
