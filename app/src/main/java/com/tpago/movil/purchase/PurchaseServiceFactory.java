package com.tpago.movil.purchase;

/**
 * Factory of {@link PurchaseService}
 */
public interface PurchaseServiceFactory {

  /**
   * Creates a new {@link PurchaseService} instance.
   *
   * @return A new {@link PurchaseService} instance.
   */
  PurchaseService create();
}
