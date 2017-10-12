package com.tpago.movil.user;

import com.birbit.android.jobqueue.JobManager;
import com.tpago.movil.store.Store;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class UserModule {

  @Provides
  @Singleton
  UserManager userManager(JobManager jobManager, Store store) {
    return UserManager.create(jobManager, store);
  }
}
