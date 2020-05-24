package com.mono40.movil.dep.init.intro;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mono40.movil.R;
import com.mono40.movil.util.Memory;

/**
 * @author hecvasro
 */
final class IntroTabFragmentAdapter extends FragmentPagerAdapter {
  private static final int COUNT = 3;

  private final int[] artArray;
  private final int[] titleArray;
  private final int[] descriptionArray;

  IntroTabFragmentAdapter(FragmentManager fragmentManager, Context context) {
    super(fragmentManager);

    if (Memory.canDisplayImageAnimation(context)) {
      artArray = new int[] {
              R.drawable.intro_tab_art_01,
              R.drawable.intro_tab_art_02,
              R.drawable.intro_tab_art_03
      };
    } else {
      artArray = new int[] {
              R.drawable.intro_tab_art_01_static,
              R.drawable.intro_tab_art_02_static,
              R.drawable.intro_tab_art_03_static
      };
    }
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
