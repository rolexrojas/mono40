package com.tpago.movil;

import java.util.List;

/**
 * @author hecvasro
 */
public interface ProductManager {
  void sync(List<? extends Product> productList);

  List<? extends Product> getAll();
}
