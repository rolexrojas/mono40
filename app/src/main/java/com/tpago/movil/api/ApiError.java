package com.tpago.movil.api;

import com.google.gson.annotations.SerializedName;

/**
 * @author hecvasro
 */
final class ApiError {
  @SerializedName("error") EmbeddedError embedded;

  static final class EmbeddedError {
    ApiCode code;
    String description;
  }
}
