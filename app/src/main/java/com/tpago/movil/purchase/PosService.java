package com.tpago.movil.purchase;

import com.tpago.movil.Code;
import com.tpago.movil.product.Product;
import com.tpago.movil.session.User;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Responsible for the registration and de-registration of {@link Product products} to enable and
 * disable them for in-commerce purchases through POS devices
 */
public interface PosService {

  /**
   * Unregisters all the registered {@link Product products} of an {@link User user} to disable
   * in-commerce purchases through POS devices.
   *
   * @throws NullPointerException
   *   If {@code user} is {@code null}.
   */
  Completable unregisterAllProducts(User user);

  /**
   * Registers a {@link Product product} of an {@link User user} to enable in-commerce purchases
   * through POS devices.
   *
   * @throws NullPointerException
   *   If {@code user} is {@code null}.
   * @throws NullPointerException
   *   If {@code product} is {@code null}.
   * @throws NullPointerException
   *   If {@code pin} is {@code null}.
   */
  Single<Result<Placeholder>> registerProduct(User user, Product product, Code pin);

  /**
   * Starts an in-commerce purchase through a POS device using a registered {@link Product product}.
   *
   * @throws NullPointerException
   *   If {@code product} is {@code null}.
   * @throws IllegalStateException
   *   if {@code product} is not registered.
   */
  Completable startPurchase(Product product);
}
