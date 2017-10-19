package com.tpago.movil.payment;

import android.support.annotation.Nullable;

/**
 * @author hecvasro
 */
public interface CarrierStore {

  @Nullable
  Carrier findByCode(int code);
}
