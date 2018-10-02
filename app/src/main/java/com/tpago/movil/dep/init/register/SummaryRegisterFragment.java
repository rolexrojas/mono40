package com.tpago.movil.dep.init.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.FragmentActivityBase;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.app.ui.main.settings.auth.alt.AltAuthMethodFragment;
import com.tpago.movil.app.StringMapper;
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

  private boolean shouldAskForAltUnlockMethod = true;

  @Inject StringMapper stringMapper;
  @Inject AlertManager alertManager;

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  @BindView(R.id.image_view_art) ImageView artImageView;

  @OnClick(R.id.button_continue)
  final void onContinueButtonClicked() {
    this.fragmentReplacer.begin(TutorialFragment.create())
      .transition(FragmentReplacer.Transition.SRFO)
      .commit();
  }

  @OnClick(R.id.button_later)
  final void onLaterButtonClicked() {
    this.fragmentReplacer.begin(InitFragment.create())
      .transition(FragmentReplacer.Transition.FIFO)
      .commit();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Injects all the annotated dependencies.
    this.getRegisterComponent()
      .inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.fragment_register_summary, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Binds all the annotated resources, views and methods.
    this.unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onResume() {
    super.onResume();

    Drawables.startAnimationDrawable(this.artImageView);

    if (this.shouldAskForAltUnlockMethod) {
      this.shouldAskForAltUnlockMethod = false;
      this.alertManager.builder()
        .title(R.string.altUnlockMethod)
        .message(R.string.altUnlockMethodEnableQuestion)
        .positiveButtonText(R.string.enableAsVerb)
        .positiveButtonAction(
          () -> this.startActivity(
            FragmentActivityBase.createLaunchIntent(
              this.getContext(),
              AltAuthMethodFragment.creator()
            )
          )
        )
        .negativeButtonText(R.string.laterAsVerb)
        .show();
    }
  }

  @Override
  public void onPause() {
    Drawables.stopAnimationDrawable(this.artImageView);

    super.onPause();
  }

  @Override
  public void onDestroyView() {
    // Unbinds all the annotated resources, views and method.
    unbinder.unbind();

    super.onDestroyView();
  }
}
