package com.tpago.movil.user;

import com.birbit.android.jobqueue.Params;
import com.tpago.movil.api.Api;
import com.tpago.movil.job.BaseJob;
import com.tpago.movil.job.JobPriority;
import com.tpago.movil.util.StringHelper;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
abstract class UserManagerJob extends BaseJob {

  static final String TAG = "UserManagerJob";

  private static Params createConfiguration(String groupId) {
    if (!StringHelper.isNullOrEmpty(groupId)) {
      throw new IllegalArgumentException("StringHelper.isNullOrEmpty(groupId)");
    }
    return new Params(JobPriority.NORMAL)
      .groupBy(groupId)
      .addTags(TAG)
      .requireNetwork()
      .persist();
  }

  @Inject Api api;

  UserManagerJob(String groupId) {
    super(createConfiguration(groupId));
  }
}
