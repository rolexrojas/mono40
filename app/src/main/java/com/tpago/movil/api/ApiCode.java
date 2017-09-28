package com.tpago.movil.api;

import com.google.gson.annotations.SerializedName;

/**
 * @author hecvasro
 */
@Deprecated
public enum ApiCode {
  @SerializedName("0004") INCORRECT_USERNAME_AND_PASSWORD,
  @SerializedName("0005") INVALID_EMAIL,
  @SerializedName("0009") INVALID_DEVICE_ID,
  @SerializedName("0012") ALREADY_ASSOCIATED_PROFILE,
  @SerializedName("0013") INVALID_PHONE_NUMBER,
  @SerializedName("0014") UNAVAILABLE_SERVICE,
  @SerializedName("0016") INVALID_PIN,
  @SerializedName("0021") ALREADY_REGISTERED_USERNAME,
  @SerializedName("0055") ALREADY_REGISTERED_EMAIL,
  @SerializedName("0112") ALREADY_ASSOCIATED_DEVICE,
  @SerializedName("0144") UNASSOCIATED_PROFILE,
  @SerializedName("0216") UNASSOCIATED_PHONE_NUMBER,
  @SerializedName("1618") INVALID_PASSWORD
}
