package com.mono40.movil.session;

import com.birbit.android.jobqueue.Params;
import com.mono40.movil.api.Api;
import com.mono40.movil.job.BaseJob;
import com.mono40.movil.job.JobPriority;
import com.mono40.movil.util.StringHelper;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
abstract class SessionJob extends BaseJob {

  static final String TAG = "SessionJob";

  private static Params createConfiguration(String groupId) {
    StringHelper.checkIsNotNullNorEmpty(groupId, "groupId");

    return new Params(JobPriority.NORMAL)
      .groupBy(groupId)
      .addTags(TAG)
      .requireNetwork()
      .persist();
  }

  @Inject Api api;
  @Inject SessionManager sessionManager;

  SessionJob(String groupId) {
    super(createConfiguration(groupId));
  }
}
