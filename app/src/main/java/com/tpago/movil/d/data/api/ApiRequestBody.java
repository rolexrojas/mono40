package com.tpago.movil.d.data.api;

import androidx.annotation.NonNull;

import java.util.HashMap;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public final class ApiRequestBody extends HashMap<String, String> {
  private ApiRequestBody() {
  }

  /**
   * TODO
   */
  public static final class Builder {
    /**
     * TODO
     */
    private final ApiRequestBody body;

    /**
     * TODO
     */
    public Builder() {
      body = new ApiRequestBody();
    }

    /**
     * TODO
     *
     * @param key
     *   TODO
     * @param value
     *   TODO
     *
     * @return TODO
     */
    @NonNull
    public final Builder putProperty(@NonNull String key, @NonNull String value) {
      body.put(key, value);
      return this;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    @NonNull
    public final ApiRequestBody build() {
      return body;
    }
  }
}
