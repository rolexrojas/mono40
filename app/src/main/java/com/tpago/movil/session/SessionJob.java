package com.tpago.movil.session;

import com.birbit.android.jobqueue.Params;
import com.tpago.movil.api.Api;
import com.tpago.movil.job.BaseJob;
import com.tpago.movil.job.JobPriority;
import com.tpago.movil.util.StringHelper;

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
