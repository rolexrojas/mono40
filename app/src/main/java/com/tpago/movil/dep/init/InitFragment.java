package com.tpago.movil.dep.init;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.init.unlock.UnlockFragment;
import com.tpago.movil.app.ui.ActivityQualifier;
import com.tpago.movil.app.ui.FragmentReplacer;
import com.tpago.movil.app.ui.permission.PermissionRequestResult;
import com.tpago.movil.app.upgrade.AppUpgradeManager;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.app.ui.permission.PermissionHelper;
import com.tpago.movil.d.domain.InitialDataLoader;
import com.tpago.movil.d.ui.main.DepMainActivity;
import com.tpago.movil.dep.init.intro.IntroFragment;
import com.tpago.movil.reactivex.DisposableHelper;
import com.tpago.movil.session.SessionManager;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class InitFragment extends BaseInitFragment {

  private static final int REQUEST_CODE = 0;

  private static final List<String> REQUIRED_PERMISSIONS = Arrays
    .asList(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS);

  @Inject AppUpgradeManager upgradeManager;
  @Inject SessionManager sessionManager;

  @Inject AlertManager alertManager;
  @Inject StringMapper stringMapper;
  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  @Inject LogoAnimator logoAnimator;
  @Inject InitialDataLoader initialDataLoader;

  private Disposable upgradeDisposable = Disposables.disposed();
  private Disposable loadInitDataDisposable = Disposables.disposed();

  private boolean werePermissionsRequested = false;
  private boolean shouldLoadInitData = false;

  public static InitFragment create() {
    return new InitFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Injects all annotated dependencies.
    this.getInitComponent()
      .inject(this);
  }

  private void handleError(Throwable throwable, String message) {
    Timber.e(throwable, message);
    this.alertManager.show(AlertData.createForGenericFailure(this.stringMapper));
  }

  private void resetAndStartLogoAnimator(Disposable disposable) {
    this.logoAnimator.reset();
    this.logoAnimator.start();
  }

  private void handleLoadInitDataSuccess() {
    final Activity activity = this.getActivity();
    activity.startActivity(DepMainActivity.getLaunchIntent(activity));
    activity.finish();
  }

  private void handleLoadInitDataError(Throwable throwable) {
    this.handleError(throwable, "Loading initial data");
  }

  private void loadInitData_() {
    this.initialDataLoader.load()
      .await();
  }

  private void loadInitData() {
    if (!this.sessionManager.isUserSet()) {
      this.fragmentReplacer.begin(IntroFragment.create())
        .transition(FragmentReplacer.Transition.SRFO)
        .commit();
    } else if (!this.sessionManager.isSessionOpen()) {
      this.fragmentReplacer.begin(UnlockFragment.create())
        .transition(FragmentReplacer.Transition.FIFO)
        .commit();
    } else {
      this.loadInitDataDisposable = Completable.fromAction(this::loadInitData_)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(this::resetAndStartLogoAnimator)
        .subscribe(this::handleLoadInitDataSuccess, this::handleLoadInitDataError);
    }
  }

  private void handleUpgradeSuccess() {
    final Context context = this.getContext();
    if (PermissionHelper.areGranted(context, REQUIRED_PERMISSIONS)) {
      this.loadInitData();
    } else if (!this.werePermissionsRequested) {
      this.werePermissionsRequested = true;
      PermissionHelper.requestPermissions(this, REQUEST_CODE, REQUIRED_PERMISSIONS);
    } else {
      // TODO: Let the user know that those permissions are required.
    }
  }

  private void handleUpgradeError(Throwable throwable) {
    this.handleError(throwable, "Upgrading");
  }

  @Override
  public void onStart() {
    super.onStart();
    this.upgradeDisposable = this.upgradeManager.upgrade()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(this::handleUpgradeSuccess, this::handleUpgradeError);
  }

  @Override
  public void onResume() {
    super.onResume();
    if (this.shouldLoadInitData) {
      this.shouldLoadInitData = false;
      this.loadInitData();
    }
  }

  @Override
  public void onStop() {
    DisposableHelper.dispose(this.loadInitDataDisposable);
    DisposableHelper.dispose(this.upgradeDisposable);
    super.onStop();
  }

  @Override
  public void onRequestPermissionsResult(
    int requestCode,
    @NonNull String[] permissions,
    @NonNull int[] results
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, results);
    if (requestCode == REQUEST_CODE) {
      final PermissionRequestResult result = PermissionRequestResult.create(permissions, results);
      if (result.isSuccessful()) {
        this.shouldLoadInitData = true;
      }
    }
  }
}
