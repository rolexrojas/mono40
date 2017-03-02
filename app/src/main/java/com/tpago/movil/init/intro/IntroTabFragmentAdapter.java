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
      R.string.intro_tab_title_first,
      R.string.intro_tab_title_second,
      R.string.intro_tab_title_third
    };
    descriptions = new int[] {
      R.string.intro_tab_description_first,
      R.string.intro_tab_description_second,
      R.string.intro_tab_description_third
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
