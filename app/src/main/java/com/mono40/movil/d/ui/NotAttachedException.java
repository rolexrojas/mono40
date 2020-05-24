package com.mono40.movil.d.ui;

import androidx.annotation.NonNull;

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
