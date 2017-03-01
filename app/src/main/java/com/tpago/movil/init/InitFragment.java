package com.tpago.movil.init;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.Session;
import com.tpago.movil.UserStore;
import com.tpago.movil.app.ActivityQualifier;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.app.PermissionRequestResult;
import com.tpago.movil.app.Permissions;
import com.tpago.movil.init.intro.IntroFragment;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public final class InitFragment extends BaseInitFragment {
  private static final int REQUEST_CODE_PHONE = 0;

  @Inject UserStore userStore;
  @Inject Session.Builder sessionBuilder;

  @Inject @ActivityQualifier FragmentReplacer fragmentReplacer;

  @Inject LogoAnimator logoAnimator;

  public static InitFragment create() {
    return new InitFragment();
  }

  private void resolve() {
    userStore.clear();
    if (!userStore.isSet()) {
      fragmentReplacer.begin(IntroFragment.create())
        .setTransition(FragmentReplacer.Transition.SRFO)
        .commit();
    } else if (!sessionBuilder.canBuild()) {
      // TODO: Sign in.
    } else {
      // TODO: Initial load.
    }
  }

  @Override
  public void onRequestPermissionsResult(
    int requestCode,
    @NonNull String[] permissions,
    @NonNull int[] results) {
    super.onRequestPermissionsResult(requestCode, permissions, results);
    final PermissionRequestResult result = PermissionRequestResult.create(permissions, results);
    if (requestCode == REQUEST_CODE_PHONE) {
      if (result.isSuccessful()) {
        resolve();
      } else {
        // TODO: Let the user know that those permissions are required.
      }
    }
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
    final Context context = getContext();
    if (Permissions.checkIfGranted(context, Manifest.permission.READ_PHONE_STATE)
      && Permissions.checkIfGranted(context, Manifest.permission.READ_SMS)) {
      resolve();
    } else {
      Permissions.requestPermissions(
        this,
        REQUEST_CODE_PHONE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_SMS);
    }
  }
}
