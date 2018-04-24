package com.tpago.movil.purchase;

import com.tpago.movil.Code;
import com.tpago.movil.product.Product;
import com.tpago.movil.session.User;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Responsible for the registration and de-registration of {@link Product products} to enable and
 * disable them for in-commerce purchases through POS devices
 */
public interface PosService {

  /**
   * Registers all the {@link Product products} of an {@link User user} to enable in-commerce
   * purchases through POS devices.
   *
   * @throws NullPointerException
   *   If {@code user} is {@code null}.
   * @throws NullPointerException
   *   If {@code products} is {@code null} or empty.
   * @throws NullPointerException
   *   If {@code pin} is {@code null}.
   */
  Single<Map<Product, Result<Placeholder>>> register(User user, List<Product> products, Code pin);

  /**
   * Unregisters all the {@link Product products} of an {@link User user} to disable in-commerce
   * purchases through POS devices.
   *
   * @throws NullPointerException
   *   If {@code user} is {@code null}.
   */
  Completable unregister(User user);

  /**
   * Starts listening for POS devices for an in-commerce purchase using a registered {@link Product
   * product}.
   *
   * @throws NullPointerException
   *   If {@code product} is {@code null}.
   * @throws IllegalStateException
   *   if {@code product} is not registered.
   */
  Completable start(Product product);
}
