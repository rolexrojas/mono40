package com.tpago.movil.d.ui.index;

import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.ActivityScope;
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
