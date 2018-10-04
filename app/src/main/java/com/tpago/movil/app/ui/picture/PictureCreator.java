package com.tpago.movil.app.ui.picture;

import android.support.v4.app.FragmentManager;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.app.ui.fragment.FragmentHelper;
import com.tpago.movil.util.function.Consumer;
import com.tpago.movil.util.ObjectHelper;

import java.io.File;

/**
 * @author hecvasro
 */
public final class PictureCreator {

  private static final String TAG = PictureCreator.class.getCanonicalName();

  static PictureCreator create(FragmentManager fragmentManager) {
    return new PictureCreator(fragmentManager);
  }

  private final FragmentManager fragmentManager;

  private Request currentRequest;

  private PictureCreator(FragmentManager fragmentManager) {
    this.fragmentManager = ObjectHelper.checkNotNull(fragmentManager, "fragmentManager");
  }

  final boolean isRequestActive() {
    return ObjectHelper.isNotNull(this.currentRequest);
  }

  private void checkRequestIsActive() {
    if (!this.isRequestActive()) {
      throw new IllegalStateException("!this.isRequestActive()");
    }
  }

  private void checkRequestIsNotActive() {
    if (this.isRequestActive()) {
      throw new IllegalStateException("this.isRequestActive()");
    }
  }

  public final void create(boolean creation, Consumer<File> consumer) {
    this.checkRequestIsNotActive();

    this.currentRequest = Request.builder()
      .creation(creation)
      .consumer(consumer)
      .build();

    PictureCreatorDialogFragment.create()
      .show(this.fragmentManager, TAG);
  }

  final Request currentRequest() {
    this.checkRequestIsActive();

    return this.currentRequest;
  }

  final void resolveRequest(File file) {
    this.checkRequestIsActive();

    this.currentRequest.consumer()
      .accept(ObjectHelper.checkNotNull(file, "file"));
    this.currentRequest = null;

    FragmentHelper.dismissByTag(this.fragmentManager, TAG);
  }

  final void onDestroy() {
    if (this.isRequestActive()) {
      this.currentRequest = null;
    }
  }

  @AutoValue
  static abstract class Request {

    static Builder builder() {
      return new AutoValue_PictureCreator_Request.Builder();
    }

    Request() {
    }

    abstract boolean creation();

    abstract Consumer<File> consumer();

    @Memoized
    @Override
    public abstract String toString();

    @Memoized
    @Override
    public abstract int hashCode();

    @AutoValue.Builder
    static abstract class Builder {

      Builder() {
      }

      abstract Builder creation(boolean isCreation);

      abstract Builder consumer(Consumer<File> consumer);

      abstract Request build();
    }
  }
}
