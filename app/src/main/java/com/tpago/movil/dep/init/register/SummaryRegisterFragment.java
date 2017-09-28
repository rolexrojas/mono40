package com.tpago.movil.dep.init.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.ActivityQualifier;
import com.tpago.movil.app.ui.FragmentReplacer;
import com.tpago.movil.dep.graphics.Drawables;
import com.tpago.movil.dep.init.InitFragment;
import com.tpago.movil.dep.init.tutorial.TutorialFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class SummaryRegisterFragment extends BaseRegisterFragment {
  static SummaryRegisterFragment create() {
    return new SummaryRegisterFragment();
  }

  private Unbinder unbinder;

  @Inject @ActivityQualifier FragmentReplacer fragmentReplacer;

  @BindView(R.id.image_view_art) ImageView artImageView;

  @OnClick(R.id.button_continue)
  void onContinueButtonClicked() {
    fragmentReplacer.begin(TutorialFragment.create())
      .transition(FragmentReplacer.Transition.SRFO)
      .commit();
  }

  @OnClick(R.id.button_later)
  void onLaterButtonClicked() {
    fragmentReplacer.begin(InitFragment.create())
      .transition(FragmentReplacer.Transition.FIFO)
      .commit();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    getRegisterComponent().inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_register_summary, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onResume() {
    super.onResume();
    Drawables.startAnimationDrawable(artImageView);
  }

  @Override
  public void onPause() {
    super.onPause();
    Drawables.stopAnimationDrawable(artImageView);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all the annotated resources, views and method.
    unbinder.unbind();
  }
}
