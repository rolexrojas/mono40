package com.tpago.movil.init.intro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tpago.movil.R;

/**
 * @author hecvasro
 */
final class IntroTabFragmentAdapter extends FragmentPagerAdapter {
  private static final int COUNT = 3;

  private final int[] arts;
  private final int[] titles;
  private final int[] descriptions;

  IntroTabFragmentAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
    arts = new int[COUNT];
    titles = new int[] {
      R.string.intro_tab_title_welcome,
      R.string.intro_tab_title_purchase,
      R.string.intro_tab_title_money
    };
    descriptions = new int[] {
      R.string.intro_tab_description_welcome,
      R.string.intro_tab_description_purchase,
      R.string.intro_tab_description_money
    };
  }

  @Override
  public Fragment getItem(int position) {
    return IntroTabFragment.create(arts[position], titles[position], descriptions[position]);
  }

  @Override
  public int getCount() {
    return COUNT;
  }
}
