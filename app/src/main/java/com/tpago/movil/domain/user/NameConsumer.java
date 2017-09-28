package com.tpago.movil.domain.user;

/**
 * Functional interfaces that accepts all the changes to the name of the {@link User user}.
 *
 * @author hecvasro
 */
public interface NameConsumer {

  /**
   * Accepts the name of an {@link User user}.
   */
  void accept(String firstName, String lastName);
}
