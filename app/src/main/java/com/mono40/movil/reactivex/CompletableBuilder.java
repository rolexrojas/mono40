package com.mono40.movil.reactivex;

import com.mono40.movil.util.ObjectHelper;

import io.reactivex.Completable;
import io.reactivex.functions.Action;

/**
 * @author hecvasro
 */
public final class CompletableBuilder {

  public static CompletableBuilder create() {
    return new CompletableBuilder();
  }

  private Completable completable = null;

  private CompletableBuilder() {
  }

  public final CompletableBuilder add(Completable completable) {
    ObjectHelper.checkNotNull(completable, "completable");
    if (ObjectHelper.isNull(this.completable)) {
      this.completable = completable;
    } else {
      this.completable = this.completable.concatWith(completable);
    }
    return this;
  }

  public final CompletableBuilder add(Action action) {
    return this.add(Completable.fromAction(action));
  }

  public final Completable build() {
    if (ObjectHelper.isNull(this.completable)) {
      return Completable.complete();
    } else {
      return this.completable;
    }
  }
}
