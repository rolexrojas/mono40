package com.tpago.movil.app.ui.activity;

/**
 * @author hecvasro
 */
public interface NavButtonPressEventConsumer {

  /**
   * @return True if the event was accepted, otherwise false.
   */
  boolean accept();
}
