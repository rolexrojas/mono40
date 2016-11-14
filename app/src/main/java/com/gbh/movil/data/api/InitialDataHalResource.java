package com.gbh.movil.data.api;

import ch.halarious.core.HalResource;

/**
 * TODO
 *
 * @author hecvasro
 */
class InitialDataHalResource implements HalResource {
  QueryHalResource query;

  @Override
  public String toString() {
    return InitialDataHalResource.class.getSimpleName() + ":{query=" + query + "}";
  }
}
