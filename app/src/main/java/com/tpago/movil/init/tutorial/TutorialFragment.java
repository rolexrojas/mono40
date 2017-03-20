package com.tpago.movil.init.tutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tpago.movil.R;
import com.tpago.movil.app.ActivityQualifier;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.init.BaseInitFragment;
import com.tpago.movil.init.InitFragment;
import com.tpago.movil.util.Preconditions;
import com.tpago.movil.widget.AutoTabSwitcher;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class TutorialFragment extends BaseInitFragment {
  private static final int TAB_COUNT = 4;

  public static TutorialFragment create() {
    return new TutorialFragment();
  }

  private Unbinder unbinder;
  private AutoTabSwitcher autoTabSwitcher;
  private AutoTabUpdater autoTabUpdater;

  @Inject @ActivityQualifier FragmentReplacer fragmentReplacer;

  @BindView(R.id.image_view_art) ImageView artImageView;
  @BindView(R.id.tab_layout) TabLayout tabLayout;
  @BindView(R.id.view_pager) ViewPager viewPager;
  @BindView(R.id.view_highlight) View highlightView;

  @OnClick(R.id.button_skip)
  void onSkipButtonClicked() {
    fragmentReplacer.begin(InitFragment.create())
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
    return inflater.inflate(R.layout.fragment_tutorial, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Initializes the view pager and the tab layout.
    viewPager.setAdapter(new TabAdapter(getChildFragmentManager()));
    tabLayout.setupWithViewPager(viewPager);
    // Creates the auto tab switcher.
    autoTabSwitcher = new AutoTabSwitcher(viewPager);
    // Creates the auto art setter.
    autoTabUpdater = new AutoTabUpdater(viewPager, artImageView, highlightView);
  }

  @Override
  public void onResume() {
    super.onResume();
    // Starts the auto tab switcher.
    autoTabSwitcher.start();
    // Starts the auto art setter.
    autoTabUpdater.start();
  }

  @Override
  public void onPause() {
    super.onPause();
    // Stops the auto art setter.
    autoTabUpdater.stop();
    // Stops the auto tab switcher.
    autoTabSwitcher.stop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Destroys the auto art setter.
    autoTabUpdater = null;
    // Destroys the auto tab switcher.
    autoTabSwitcher = null;
    // Unbinds all the annotated resources, views and method.
    unbinder.unbind();
  }

  private static class TabAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private static final List<Integer> TAB_LIST_TITLE = Arrays.asList(
      R.string.tutorial_tab_label_title_01,
      R.string.tutorial_tab_label_title_02,
      R.string.tutorial_tab_label_title_03,
      R.string.tutorial_tab_label_title_04);
    private static final List<Integer> TAB_LIST_DESCRIPTION = Arrays.asList(
      R.string.tutorial_tab_label_description_01,
      R.string.tutorial_tab_label_description_02,
      R.string.tutorial_tab_label_description_03,
      R.string.tutorial_tab_label_description_04
    );

    TabAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
      return TutorialTabFragment.create(
        TAB_LIST_TITLE.get(position),
        TAB_LIST_DESCRIPTION.get(position));
    }

    @Override
    public int getCount() {
      return TAB_COUNT;
    }
  }

  private static class AutoTabUpdater extends ViewPager.SimpleOnPageChangeListener
    implements View.OnClickListener {
    private static final List<Integer> TAB_LIST_ART = Arrays.asList(
      R.drawable.tutorial_tab_art_01,
      R.drawable.tutorial_tab_art_01,
      R.drawable.tutorial_tab_art_01,
      R.drawable.tutorial_tab_art_04);

    private final ViewPager viewPager;
    private final ImageView imageView;
    private final View highlightView;

    AutoTabUpdater(ViewPager viewPager, ImageView imageView, View highlightView) {
      this.viewPager = Preconditions.checkNotNull(viewPager, "viewPager == null");
      this.imageView = Preconditions.checkNotNull(imageView, "imageView == null");
      this.highlightView = Preconditions.checkNotNull(highlightView, "highlightView");
    }

    private void update(int position) {
      imageView.setImageResource(TAB_LIST_ART.get(position));
//      if (position == 0 || position == 3) {
        highlightView.setVisibility(View.GONE);
//      } else {
//        final int x;
//        final Resources r = highlightView.getResources();
//        final int xOffset = r.getDimensionPixelOffset(R.dimen.tutorial_tab_highlight_offset_x);
//        if (position == 1) {
//          x = imageView.getRight() - highlightView.getWidth() - xOffset;
//        } else {
//          x = imageView.getLeft() + xOffset;
//        }
//        highlightView.setX(x);
//        final int yOffset = r.getDimensionPixelOffset(R.dimen.tutorial_tab_highlight_offset_y);
//        highlightView.setY(imageView.getTop() + yOffset);
//        highlightView.setVisibility(View.VISIBLE);
//      }
    }

    final void start() {
      highlightView.setOnClickListener(this);
      viewPager.addOnPageChangeListener(this);
      update(viewPager.getCurrentItem());
    }

    final void stop() {
      viewPager.removeOnPageChangeListener(this);
      highlightView.setOnClickListener(null);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
      if (state == ViewPager.SCROLL_STATE_SETTLING) {
        update(viewPager.getCurrentItem());
      }
    }

    @Override
    public void onClick(View view) {
      if (view.equals(highlightView)) {
        final int position = viewPager.getCurrentItem();
        viewPager.setCurrentItem((position + 1) % TAB_COUNT);
      }
    }
  }
}
