package com.tpago.movil.store;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

/**
 * {@link Store} implementation that uses JSON as format and {@link SharedPreferences} for storage
 * <p>
 * {@link com.google.gson.Gson} is used for serialization and deserialization to and from JSON.
 */
public final class DiskStore implements Store {

  static Builder builder() {
    return new Builder();
  }

  private final SharedPreferences preferences;
  private final Gson gson;

  private DiskStore(Builder builder) {
    this.preferences = builder.preferences;
    this.gson = builder.gson;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSet(String key) {
    StringHelper.checkIsNotNullNorEmpty(key, "key");
    return this.preferences.contains(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void set(String key, T value) {
    StringHelper.checkIsNotNullNorEmpty(key, "key");
    this.preferences.edit()
      .putString(key, this.gson.toJson(value))
      .apply();
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public <T> T get(String key, Class<T> valueType) {
    StringHelper.checkIsNotNullNorEmpty(key, "key");
    ObjectHelper.checkNotNull(valueType, "valueType");
    if (!this.isSet(key)) {
      return null;
    }
    return this.gson.fromJson(this.preferences.getString(key, null), valueType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove(String key) {
    StringHelper.checkIsNotNullNorEmpty(key, "key");
    if (this.isSet(key)) {
      this.preferences.edit()
        .remove(key)
        .apply();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    this.preferences.edit()
      .clear()
      .apply();
  }

  static final class Builder {

    private SharedPreferences preferences;
    private Gson gson;

    private Builder() {
    }

    final Builder preferences(SharedPreferences preferences) {
      this.preferences = ObjectHelper.checkNotNull(preferences, "preferences");
      return this;
    }

    final Builder gson(Gson gson) {
      this.gson = ObjectHelper.checkNotNull(gson, "gson");
      return this;
    }

    final DiskStore build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("preferences", ObjectHelper.isNull(this.preferences))
        .addPropertyNameIfMissing("gson", ObjectHelper.isNull(this.gson))
        .checkNoMissingProperties();
      return new DiskStore(this);
    }
  }
}
