package com.tpago.movil.job;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.di.DependencyInjector;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class JobModule {

  public static JobModule create(DependencyInjector dependencyInjector) {
    return new JobModule(dependencyInjector);
  }

  private final DependencyInjector dependencyInjector;

  private JobModule(DependencyInjector dependencyInjector) {
    this.dependencyInjector = ObjectHelper.checkNotNull(dependencyInjector, "dependencyInjector");
  }

  @Provides
  @Singleton
  JobManager jobManager(Context context) {
    final Configuration configuration = new Configuration.Builder(context)
      .customLogger(JobLogger.create())
      .injector(this.dependencyInjector)
      .build();
    return new JobManager(configuration);
  }
}
