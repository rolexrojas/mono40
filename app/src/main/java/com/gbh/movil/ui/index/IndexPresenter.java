package com.gbh.movil.ui.index;

import android.Manifest;
import android.support.annotation.NonNull;

import com.gbh.movil.domain.session.SessionManager;
import com.gbh.movil.misc.rx.RxUtils;
import com.gbh.movil.ui.Presenter;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * TODO
 *
 * @author hecvasro
 */
final class IndexPresenter extends Presenter<IndexScreen> {
  private static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
  private static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;

  private final RxPermissions permissionManager;
  private final SessionManager sessionManager;

  /**
   * TODO
   */
  private Subscription permissionRequestSubscription = Subscriptions.unsubscribed();

  IndexPresenter(@NonNull RxPermissions permissionManager,
    @NonNull SessionManager sessionManager) {
    this.permissionManager = permissionManager;
    this.sessionManager = sessionManager;
  }

  /**
   * TODO
   */
  final void create() {
    permissionRequestSubscription = permissionManager.request(PERMISSION_READ_PHONE_STATE,
      PERMISSION_READ_SMS)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Boolean>() {
        @Override
        public void call(Boolean isGranted) {
          if (isGranted) {
            if (sessionManager.isActive()) {
              screen.startSignInScreen();
            } else {
              screen.startAuthIndexScreen();
            }
            screen.finish();
          } else {
            // TODO: Let the user know that permission is required.
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Requesting '%1$s' and '%2$s' permissions",
            PERMISSION_READ_PHONE_STATE, PERMISSION_READ_SMS);
        }
      });
  }

  /**
   * TODO
   */
  final void destroy() {
    RxUtils.unsubscribe(permissionRequestSubscription);
  }
}
