package com.tpago.movil.partner;

import android.support.annotation.Nullable;

/**
 * @author hecvasro
 */
public interface CarrierStore {

  @Nullable
  Carrier findByCode(int code);
}
