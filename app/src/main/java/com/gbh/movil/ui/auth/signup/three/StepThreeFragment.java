package com.gbh.movil.ui.auth.signup.three;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.R;
import com.gbh.movil.ui.ChildFragment;
import com.gbh.movil.ui.auth.signup.SignUpContainer;
import com.gbh.movil.ui.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class StepThreeFragment extends ChildFragment<SignUpContainer> {
  private Unbinder unbinder;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static StepThreeFragment newInstance() {
    return new StepThreeFragment();
  }

  @OnClick(R.id.button_later)
  void onLaterButtonClicked() {
    final Activity activity = getActivity();
    startActivity(MainActivity.getLaunchIntent(activity));
    activity.finish();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.screen_sign_up_step_three, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }
}
