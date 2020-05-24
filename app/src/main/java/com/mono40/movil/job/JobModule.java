package com.mono40.movil.job;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.di.DependencyInjector;
import com.birbit.android.jobqueue.persistentQueue.sqlite.SqliteJobQueue;
import com.google.gson.Gson;
import com.mono40.movil.session.UpdateUserCarrierJob;
import com.mono40.movil.session.UpdateUserNameJob;
import com.mono40.movil.session.UpdateUserPictureJob;
import com.mono40.movil.util.ObjectHelper;

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
  SqliteJobQueue.JobSerializer jobSerializer(Gson gson) {
    return JsonJobSerializer.builder()
      .gson(gson)
      .addJob(UpdateUserNameJob.class, UpdateUserNameJob.TYPE)
      .addJob(UpdateUserCarrierJob.class, UpdateUserCarrierJob.TYPE)
      .addJob(UpdateUserPictureJob.class, UpdateUserPictureJob.TYPE)
      .build();
  }

  @Provides
  @Singleton
  JobManager jobManager(Context context, SqliteJobQueue.JobSerializer jobSerializer) {
    final Configuration configuration = new Configuration.Builder(context)
      .customLogger(JobLogger.create())
      .injector(this.dependencyInjector)
      .jobSerializer(jobSerializer)
      .build();
    return new JobManager(configuration);
  }
}
