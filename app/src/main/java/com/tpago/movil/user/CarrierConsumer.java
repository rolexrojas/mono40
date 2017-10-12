package com.tpago.movil.user;

import com.tpago.movil.payment.Carrier;

/**
 * @author hecvasro
 */
public interface CarrierConsumer {

  void accept(Carrier carrier);
}
