package com.tpago.movil.d.domain.pos;

import com.tpago.movil.PhoneNumber;
import com.tpago.movil.d.domain.Product;

import rx.Single;

/**
 * @author hecvasro
 */
@Deprecated
public interface PosBridge {

  boolean isAvailable();

  boolean isRegistered(Product product);

  PosResult addCard(String phoneNumber, String pin, Product product);

  PosResult removeCard(Product product);

  void unregister(PhoneNumber phoneNumber) throws Exception;

  Single<PosResult> selectCard(Product product);
}
