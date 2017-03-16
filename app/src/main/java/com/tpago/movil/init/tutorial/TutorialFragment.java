package com.tpago.movil.init.tutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.app.ActivityQualifier;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.init.BaseInitFragment;
import com.tpago.movil.init.InitFragment;
import com.tpago.movil.util.Preconditions;
import com.tpago.movil.widget.AutoTabSwitcher;

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

  private static final int[] TAB_ARRAY_ART = new int[] {
    R.drawable.tutorial_tab_art_01,
    R.drawable.tutorial_tab_art_01,
    R.drawable.tutorial_tab_art_01,
    R.drawable.tutorial_tab_art_04
  };
  private static final int[] TAB_ARRAY_TITLE = new int[] {
    R.string.tutorial_tab_label_title_01,
    R.string.tutorial_tab_label_title_02,
    R.string.tutorial_tab_label_title_03,
    R.string.tutorial_tab_label_title_04
  };
  private static final int[] TAB_ARRAY_DESCRIPTION = new int[] {
    R.string.tutorial_tab_label_description_01,
    R.string.tutorial_tab_label_description_02,
    R.string.tutorial_tab_label_description_03,
    R.string.tutorial_tab_label_description_04
  };

  public static TutorialFragment create() {
    return new TutorialFragment();
  }

  private Unbinder unbinder;
  private AutoTabSwitcher autoTabSwitcher;
  private AutoArtSetter autoArtSetter;

  @Inject @ActivityQualifier FragmentReplacer fragmentReplacer;

  @BindView(R.id.image_view_art) ImageView artImageView;
  @BindView(R.id.tab_layout) TabLayout tabLayout;
  @BindView(R.id.view_pager) ViewPager viewPager;

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
    viewPager.setAdapter(new TutorialTabFragmentAdapter(
      getChildFragmentManager(),
      TAB_COUNT,
      TAB_ARRAY_TITLE,
      TAB_ARRAY_DESCRIPTION));
    tabLayout.setupWithViewPager(viewPager);
    // Creates the auto tab switcher.
    autoTabSwitcher = new AutoTabSwitcher(viewPager);
    // Creates the auto art setter.
    autoArtSetter = new AutoArtSetter(viewPager, artImageView, TAB_ARRAY_ART);
  }

  @Override
  public void onResume() {
    super.onResume();
    // Starts the auto tab switcher.
    autoTabSwitcher.start();
    // Starts the auto art setter.
    autoArtSetter.start();
  }

  @Override
  public void onPause() {
    super.onPause();
    // Stops the auto art setter.
    autoArtSetter.stop();
    // Stops the auto tab switcher.
    autoTabSwitcher.stop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Destroys the auto art setter.
    autoArtSetter = null;
    // Destroys the auto tab switcher.
    autoTabSwitcher = null;
    // Unbinds all the annotated resources, views and method.
    unbinder.unbind();
  }

  private static class AutoArtSetter extends ViewPager.SimpleOnPageChangeListener {
    private final ViewPager viewPager;
    private final ImageView imageView;
    private final int[] artArray;

    AutoArtSetter(ViewPager viewPager, ImageView imageView, int[] artArray) {
      this.viewPager = Preconditions.checkNotNull(viewPager, "viewPager == null");
      this.imageView = Preconditions.checkNotNull(imageView, "imageView == null");
      this.artArray = Preconditions.checkNotNull(artArray, "artArray == null");
    }

    final void start() {
      viewPager.addOnPageChangeListener(this);
      imageView.setImageResource(artArray[viewPager.getCurrentItem()]);
    }

    final void stop() {
      viewPager.removeOnPageChangeListener(this);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
      if (state == ViewPager.SCROLL_STATE_SETTLING) {
        Picasso.with(viewPager.getContext())
          .load(artArray[viewPager.getCurrentItem()])
          .noFade()
          .into(imageView);
      }
    }
  }
}
