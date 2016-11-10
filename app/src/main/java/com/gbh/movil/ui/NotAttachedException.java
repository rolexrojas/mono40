package com.gbh.movil.ui;

import android.support.annotation.NonNull;

/**
 * TODO
 *
 * @author hecvasro
 */
public class NotAttachedException extends RuntimeException {
  public NotAttachedException() {
    super();
  }

  public NotAttachedException(@NonNull String message) {
    super(message);
  }
}
