package com.tpago.movil.ui.auth.signup.three;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.ui.ChildFragment;
import com.tpago.movil.ui.auth.signup.SignUpContainer;
import com.tpago.movil.ui.main.MainActivity;

import butterknife.BindView;
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

  @BindView(R.id.text_view_emoji)
  TextView emojiTextView;

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
    if (activity.getCallingActivity() == null) {
      startActivity(MainActivity.getLaunchIntent(activity));
    } else {
      activity.setResult(Activity.RESULT_OK);
    }
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
    // Sets the emoji of the screen.
    emojiTextView.setText(new String(Character.toChars(0x1F44D)));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }
}
