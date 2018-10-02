package com.tpago.movil.app.upgrade;

import com.tpago.movil.app.upgrade.action.AppUpgradeAction;
import com.tpago.movil.store.DiskStore;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import io.reactivex.Completable;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class AppUpgradeManager {

  private static final String STORE_KEY_ACTION_ID = "AppUpgradeManager.ActionId";

  public static Builder builder() {
    return new Builder();
  }

  private final DiskStore diskStore;
  private final List<AppUpgradeAction> actions;

  private AppUpgradeManager(Builder builder) {
    this.diskStore = builder.diskStore;
    this.actions = builder.actions;
  }

  public final Completable upgrade() {
    Completable completable = Completable.complete();
    final int lastActionId = this.diskStore.get(STORE_KEY_ACTION_ID, Integer.class)
      .defaultIfEmpty(0)
      .blockingGet();
    final int size = this.actions.size();
    if (lastActionId < size) {
      Completable actionCompletable;
      for (int i = lastActionId; i < size; i++) {
        final int actionId = i + 1;
        actionCompletable = Completable.fromAction(this.actions.get(i))
          .concatWith(this.diskStore.set(STORE_KEY_ACTION_ID, actionId))
          .doOnComplete(() -> Timber.d("Upgrade action %1$s applied", actionId));
        completable = completable.concatWith(actionCompletable);
      }
    }
    return completable;
  }

  public static final class Builder {

    private DiskStore diskStore;
    private final List<AppUpgradeAction> actions;

    private Builder() {
      this.actions = new ArrayList<>();
    }

    public final Builder store(DiskStore diskStore) {
      this.diskStore = ObjectHelper.checkNotNull(diskStore, "diskStore");
      return this;
    }

    public final Builder actions(Set<AppUpgradeAction> actions) {
      ObjectHelper.checkNotNull(actions, "actions");
      this.actions.clear();
      this.actions.addAll(actions);
      Collections.sort(this.actions, AppUpgradeAction::compareTo);
      return this;
    }

    public final AppUpgradeManager build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("diskStore", ObjectHelper.isNull(this.diskStore))
        .checkNoMissingProperties();
      return new AppUpgradeManager(this);
    }
  }
}
