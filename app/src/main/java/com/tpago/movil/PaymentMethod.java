package com.tpago.movil;

import android.os.Parcelable;

/**
 * @author hecvasro
 */
public abstract class PaymentMethod<T extends Product> implements Parcelable {
  public abstract T getProduct();
}
