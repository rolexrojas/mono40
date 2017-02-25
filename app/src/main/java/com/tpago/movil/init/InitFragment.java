package com.tpago.movil.init;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tpago.movil.UserStore;
import com.tpago.movil.app.ActivityQualifier;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.init.intro.IntroFragment;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class InitFragment extends BaseInitFragment {
  @Inject
  UserStore userStore;

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  @Inject
  LogoAnimator logoAnimator;

  public static InitFragment create() {
    return new InitFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all annotated dependencies.
    getInitComponent().inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    logoAnimator.reset();
    if (userStore.isSet()) {
      // TODO: Start the authentication process.
      Timber.d("Starting the authentication process");
    } else {
      // TODO: Request permissions
      logoAnimator.moveAndScale();
      fragmentReplacer.begin(IntroFragment.create())
        .setTransition(FragmentReplacer.Transition.SRFO)
        .commit();
    }
  }
}
