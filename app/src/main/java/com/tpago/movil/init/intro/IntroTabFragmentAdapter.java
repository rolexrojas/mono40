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

  private final int[] artArray;
  private final int[] titleArray;
  private final int[] descriptionArray;

  IntroTabFragmentAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
    artArray = new int[] {
      R.drawable.intro_tab_art_01,
      R.drawable.intro_tab_art_02,
      R.drawable.intro_tab_art_03
    };
    titleArray = new int[] {
      R.string.intro_tab_title_01,
      R.string.intro_tab_title_02,
      R.string.intro_tab_title_03
    };
    descriptionArray = new int[] {
      R.string.intro_tab_description_01,
      R.string.intro_tab_description_02,
      R.string.intro_tab_description_03
    };
  }

  @Override
  public Fragment getItem(int position) {
    return IntroTabFragment.create(
      artArray[position],
      titleArray[position],
      descriptionArray[position]);
  }

  @Override
  public int getCount() {
    return COUNT;
  }
}
