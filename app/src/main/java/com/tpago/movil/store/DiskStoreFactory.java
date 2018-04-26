package com.tpago.movil.store;

import android.content.Context;

import com.google.gson.Gson;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

/**
 * Factory of {@link DiskStore}
 */
public final class DiskStoreFactory {

  static Builder builder() {
    return new Builder();
  }

  private final Context context;
  private final Gson gson;

  private DiskStoreFactory(Builder builder) {
    this.context = builder.context;
    this.gson = builder.gson;
  }

  /**
   * Creates a new {@link DiskStore} instance.
   *
   * @return A new {@link DiskStore} instance.
   */
  public final DiskStore create(String name) {
    return DiskStore.builder()
      .preferences(this.context.getSharedPreferences(name, Context.MODE_PRIVATE))
      .gson(this.gson)
      .build();
  }

  static final class Builder {

    private Context context;
    private Gson gson;

    private Builder() {
    }

    final Builder context(Context context) {
      this.context = ObjectHelper.checkNotNull(context, "context");
      return this;
    }

    final Builder gson(Gson gson) {
      this.gson = ObjectHelper.checkNotNull(gson, "gson");
      return this;
    }

    final DiskStoreFactory build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("context", ObjectHelper.isNull(this.context))
        .addPropertyNameIfMissing("gson", ObjectHelper.isNull(this.gson))
        .checkNoMissingProperties();
      return new DiskStoreFactory(this);
    }
  }
}
