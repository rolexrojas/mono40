package com.tpago.movil.user;

import android.net.Uri;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.TagConstraint;
import com.tpago.movil.payment.Carrier;
import com.tpago.movil.store.Store;
import com.tpago.movil.util.ObjectHelper;

import java.io.File;
import java.util.function.Consumer;

/**
 * @author hecvasro
 */
public final class UserManager {

  private static final String KEY = "UserManager.Data";

  static UserManager create(JobManager jobManager, Store store) {
    return new UserManager(jobManager, store);
  }

  private final JobManager jobManager;
  private final Store store;

  private User user;
  private final Consumer<User> userChangeConsumer;

  private UserManager(JobManager jobManager, Store store) {
    this.jobManager = ObjectHelper.checkNotNull(jobManager, "jobManager");
    this.store = ObjectHelper.checkNotNull(store, "store");

    this.userChangeConsumer = (user) -> this.store.set(KEY, user);
    if (this.store.isSet(KEY)) {
      this.user = ObjectHelper.checkNotNull(this.store.get(KEY, User.class), "user");
      this.user.addChangeConsumer(this.userChangeConsumer);
    }
  }

  public final boolean isSet() {
    return ObjectHelper.isNotNull(this.user);
  }

  private void checkIsSet() {
    if (!this.isSet()) {
      throw new IllegalStateException("!this.isSet()");
    }
  }

  private void checkIsNotSet() {
    if (this.isSet()) {
      throw new IllegalStateException("this.isSet()");
    }
  }

  public final void set(User user) {
    this.checkIsNotSet();
    this.user = ObjectHelper.checkNotNull(user, "user");
    this.user.addChangeConsumer(this.userChangeConsumer);
    this.store.set(KEY, this.user);
  }

  public final User get() {
    return this.user;
  }

  public final void updateName(String firstName, String lastName) {
    this.checkIsSet();
    this.user.updateName(firstName, lastName);
    this.jobManager.addJobInBackground(UpdateNameUserManagerJob.create(firstName, lastName));
  }

  public final void updatePicture(File picture) {
    this.checkIsSet();
    this.jobManager.addJobInBackground(UpdatePictureUserManagerJob.create(picture));
  }

  final void updatePicture(Uri picture) {
    this.checkIsSet();
    this.user.updatePicture(picture);
  }

  public final void updateCarrier(Carrier carrier) {
    this.checkIsSet();
    this.user.updateCarrier(carrier);
    this.jobManager.addJobInBackground(UpdateCarrierManagerJob.create(carrier.code()));
  }

  public final void clear() {
    this.checkIsSet();
    this.jobManager.cancelJobs(TagConstraint.ALL, UserManagerJob.TAG);
    this.store.remove(KEY);
    this.user.removeChangeConsumer(this.userChangeConsumer);
    this.user = null;
  }
}
