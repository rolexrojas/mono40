package com.tpago.movil.net;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class Result<T> {
  public abstract T getData();

  public abstract boolean isSuccessful();
}
