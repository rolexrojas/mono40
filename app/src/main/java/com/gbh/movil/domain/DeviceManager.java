package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.PhoneNumber;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface DeviceManager {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  PhoneNumber getPhoneNumber();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  String getId();
}
