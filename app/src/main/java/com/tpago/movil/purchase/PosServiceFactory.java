package com.tpago.movil.purchase;

/**
 * Factory of {@link PosService}
 */
public interface PosServiceFactory {

  /**
   * Creates a new {@link PosService} instance.
   *
   * @return A new {@link PosService} instance.
   */
  PosService create();
}
