package com.tpago.movil.io;

import com.tpago.movil.util.Preconditions;

import java.io.File;

/**
 * @author hecvasro
 */
public final class Files {
  public static boolean checkIfExists(File file) {
    return Preconditions.checkNotNull(file, "file == null").exists();
  }

  private Files() {
  }
}
