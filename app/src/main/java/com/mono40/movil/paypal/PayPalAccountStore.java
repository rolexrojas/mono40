package com.mono40.movil.paypal;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * Store of {@link PayPalAccount PayPal accounts}
 */
public interface PayPalAccountStore {

  Completable sync(List<PayPalAccount> entries);

  Maybe<List<PayPalAccount>> getAll();

  Completable clear();
}
