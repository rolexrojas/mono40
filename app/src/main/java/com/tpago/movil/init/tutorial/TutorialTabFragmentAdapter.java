package com.tpago.movil.init.tutorial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
final class TutorialTabFragmentAdapter extends FragmentPagerAdapter {
  private final int tabCount;
  private final int[] titleTabArray;
  private final int[] descriptionTabArray;

  TutorialTabFragmentAdapter(
    FragmentManager fragmentManager,
    int tabCount,
    int[] titleTabArray,
    int[] descriptionTabArray) {
    super(fragmentManager);
    if (tabCount == 0) {
      throw new IllegalArgumentException("tabCount == 0");
    }
    this.tabCount = tabCount;
    this.titleTabArray = Preconditions.checkNotNull(titleTabArray, "titleTabArray == null");
    if (this.titleTabArray.length != this.tabCount) {
      throw new IllegalArgumentException("titleTabArray.length != tabCount.length");
    }
    this.descriptionTabArray = Preconditions
      .checkNotNull(descriptionTabArray, "descriptionTabArray == null");
    if (this.descriptionTabArray.length != this.tabCount) {
      throw new IllegalArgumentException("descriptionTabArray.length != tabCount.length");
    }
  }

  @Override
  public Fragment getItem(int position) {
    return TutorialTabFragment.create(titleTabArray[position], descriptionTabArray[position]);
  }

  @Override
  public int getCount() {
    return tabCount;
  }
}
