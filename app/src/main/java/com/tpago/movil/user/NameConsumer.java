package com.tpago.movil.user;

/**
 * Functional interfaces that accepts changes to the first and last names of an {@link User user}.
 *
 * @author hecvasro
 */
public interface NameConsumer {

  /**
   * Accepts the first and last names of an {@link User user}.
   */
  void accept(String firstName, String lastName);
}
