package com.tpago.movil.dep.init.unlock;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.ActivityQualifier;
import com.tpago.movil.app.ui.FragmentReplacer;
import com.tpago.movil.data.picasso.CircleTransformation;
import com.tpago.movil.dep.init.BaseInitFragment;
import com.tpago.movil.dep.init.InitFragment;
import com.tpago.movil.dep.init.LogoAnimator;
import com.tpago.movil.dep.text.BaseTextWatcher;
import com.tpago.movil.dep.widget.Keyboard;
import com.tpago.movil.dep.widget.TextInput;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class UnlockFragment extends BaseInitFragment implements UnlockPresenter.View {

  public static UnlockFragment create() {
    return new UnlockFragment();
  }

  private Unbinder unbinder;

  private TextWatcher passwordTextInputTextWatcher;

  private UnlockPresenter presenter;

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  @Inject LogoAnimator logoAnimator;

  @BindView(R.id.image_view_avatar) ImageView avatarImageView;
  @BindView(R.id.label_title) TextView titleLabel;
  @BindView(R.id.text_input_password) TextInput passwordTextInput;
  @BindView(R.id.button_unlock) Button unlockButton;

  @OnClick(R.id.button_unlock)
  final void onUnlockButtonClicked() {
    this.presenter.onUnlockButtonClicked();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Injects all the annotated dependencies.
    getInitComponent()
      .inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.fragment_unlock, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Creates the presenter.
    presenter = new UnlockPresenter(this, getInitComponent());
  }

  @Override
  public void onStart() {
    super.onStart();
    // Notifies the presenter that the view its being started.
    presenter.onViewStarted();
  }

  @Override
  public void onResume() {
    super.onResume();
    // Moves the logo out of the screen.
    logoAnimator.moveOutOfScreen();
    // Shows the keyboard.
    Keyboard.show(passwordTextInput);
    // Attaches the password text input to the presenter.
    passwordTextInputTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        presenter.onPasswordTextInputContentChanged(s.toString());
      }
    };
    passwordTextInput.addTextChangedListener(passwordTextInputTextWatcher);
    passwordTextInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          presenter.onUnlockButtonClicked();
        }
        return false;
      }
    });
  }

  @Override
  public void onPause() {
    super.onPause();
    // Detaches the last updateName text input from the presenter.
    passwordTextInput.setOnEditorActionListener(null);
    passwordTextInput.removeTextChangedListener(passwordTextInputTextWatcher);
    passwordTextInputTextWatcher = null;
  }

  @Override
  public void onStop() {
    super.onStop();
    // Notifies the presenter that the view its being stopped.
    presenter.onViewStopped();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Destroys the presenter.
    presenter = null;
    // Unbinds all the annotated resources, views and method.
    unbinder.unbind();
  }

  @Override
  public void setUserPictureUri(Uri pictureUri) {
    Picasso.with(this.getContext())
      .load(pictureUri)
      .resizeDimen(R.dimen.normalProfilePictureSize, R.dimen.normalProfilePictureSize)
      .transform(new CircleTransformation())
      .noFade()
      .into(this.avatarImageView);
  }

  @Override
  public void setUserFirstName(String userFirstName) {
    titleLabel.setText(String.format(getString(R.string.unlock_label_title), userFirstName));
  }

  @Override
  public void setPasswordTextInputContent(String content) {
    passwordTextInput.setText(content);
  }

  @Override
  public void showPasswordTextInputAsErratic(boolean showAsErratic) {
    passwordTextInput.setErraticStateEnabled(showAsErratic);
  }

  @Override
  public void setUnlockButtonEnabled(boolean enabled) {
    unlockButton.setEnabled(enabled);
  }

  @Override
  public void showUnlockButtonAsEnabled(boolean showAsEnabled) {
    unlockButton.setAlpha(showAsEnabled ? 1.0F : 0.5F);
  }

  @Override
  public void moveToInitScreen() {
    fragmentReplacer.begin(InitFragment.create())
      .transition(FragmentReplacer.Transition.FIFO)
      .commit();
  }
}
