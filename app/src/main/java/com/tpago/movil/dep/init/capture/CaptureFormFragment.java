package com.tpago.movil.dep.init.capture;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.fragment.FragmentQualifier;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.dep.InformationalDialogFragment;
import com.tpago.movil.util.UiUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public abstract class CaptureFormFragment<P extends CaptureFormPresenter>
  extends BaseCaptureFragment
  implements CaptureFormPresenter.View {
  private Unbinder unbinder;

  @Inject
  @FragmentQualifier
  FragmentReplacer fragmentReplacer;

  @Inject
  @ActivityQualifier
  FragmentReplacer activityFragmentReplacer;

  @BindView(R.id.button_move_to_next_screen)
  Button moveToNextScreenButton;

  protected abstract P getPresenter();

  protected abstract Fragment getNextScreen();

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Notifies the presenter that the view its being started.
    getPresenter().onViewStarted();
  }

  @Override
  public void onResume() {
    super.onResume();
    // Adds the listener that gets notified every time the move to the next screen button is clicked.
    moveToNextScreenButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        getPresenter().onMoveToNextScreenButtonClicked();
      }
    });
  }

  @Override
  public void onPause() {
    super.onPause();
    // Removes the listener that gets notified every time the move to the next screen button is clicked.
    moveToNextScreenButton.setOnClickListener(null);
  }

  @Override
  public void onStop() {
    super.onStop();
    // Notifies the presenter that the view its being stopped.
    getPresenter().onViewStopped();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all the annotated resources, views and methods.
    unbinder.unbind();
  }

  @Override
  public void showDialog(String title, String message, String positiveButtonText) {
    InformationalDialogFragment.create(title, message, positiveButtonText)
      .show(getChildFragmentManager(), null);
  }

  @Override
  public void setMoveToNextScreenButtonEnabled(boolean enabled) {
    moveToNextScreenButton.setEnabled(enabled);
  }

  @Override
  public void showMoveToNextScreenButtonAsEnabled(boolean showAsEnabled) {
    UiUtil.setEnabled(moveToNextScreenButton, showAsEnabled);
  }

  @Override
  public void moveToNextScreen() {
    fragmentReplacer.begin(getNextScreen())
      .addToBackStack()
      .transition(FragmentReplacer.Transition.SRFO)
      .commit();
  }
}
