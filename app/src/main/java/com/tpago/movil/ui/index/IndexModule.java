package com.tpago.movil.ui.index;

import com.tpago.movil.domain.session.SessionManager;
import com.tpago.movil.ui.ActivityScope;
import com.tbruyelle.rxpermissions.RxPermissions;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
class IndexModule {
  /**
   * TODO
   *
   * @param permissionManager
   *   TODO
   * @param sessionManager
   *   TODO
   *
   * @return TODO
   */
  @Provides
  @ActivityScope
  IndexPresenter providePresenter(RxPermissions permissionManager, SessionManager sessionManager) {
    return new IndexPresenter(permissionManager, sessionManager);
  }
}
