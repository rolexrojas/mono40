package com.tpago.movil.d.ui.main.recipient.index.category;

/**
 * Action representation.
 *
 * @author hecvasro
 */
abstract class Action {

  abstract Type type();

  enum Type {
    TRANSACTION_WITH_PHONE_NUMBER,
    ADD_PHONE_NUMBER,
    TRANSACTION_WITH_ACCOUNT,
    ADD_ACCOUNT
  }
}
