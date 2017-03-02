package com.tpago.movil.io;

import android.content.Context;
import android.os.Environment;

import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.io.File;

/**
 * @author hecvasro
 */
public final class Files {
  private static final String DIRECTORY_PICTURES = Environment.DIRECTORY_PICTURES;

  public static File createExternalPictureFile(Context context) throws IllegalStateException {
    Preconditions.checkNotNull(context, "context == null");
    final File d = context.getExternalFilesDir(DIRECTORY_PICTURES);
    if (Objects.isNull(d)) {
      throw new IllegalStateException("context.getExternalFilesDir(DIRECTORY_PICTURES) == null");
    }
    return new File(d, Long.toString(System.currentTimeMillis()));
  }

  public static File createInternalPictureFile(Context context, String name) {
    Preconditions.checkNotNull(context, "context == null");
    final File d = new File(context.getFilesDir(), DIRECTORY_PICTURES);
    if (!d.exists()) {
      d.mkdir();
    }
    return new File(d, name);
  }

  private Files() {
    throw new AssertionError("Cannot be instantiated");
  }
}
