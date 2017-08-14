package com.tpago.movil;

import com.google.auto.value.AutoValue;

import java.io.File;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Avatar {

  public static Avatar create(File file) {
    return new AutoValue_Avatar(file);
  }

  public final boolean exists() {
    return getFile().exists();
  }

  public final void clear() {
    if (exists()) {
      getFile().delete();
    }
  }

  public abstract File getFile();
}
