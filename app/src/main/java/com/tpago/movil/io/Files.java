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
  private static final String FORMAT_FILE_NAME_PICTURE = "%1$s.png";

  private static final String DIRECTORY_PICTURES = Environment.DIRECTORY_PICTURES;

  private static String formatName(String name) {
    return String.format(FORMAT_FILE_NAME_PICTURE, name);
  }

  public static File createExternalPictureFile(Context context) throws IllegalStateException {
    Preconditions.checkNotNull(context, "context == null");
    final File d = context.getExternalFilesDir(DIRECTORY_PICTURES);
    if (Objects.isNull(d)) {
      throw new IllegalStateException("context.getExternalFilesDir(DIRECTORY_PICTURES) == null");
    }
    if (!d.exists()) {
      d.mkdirs();
    }
    return new File(d, formatName(Long.toString(System.currentTimeMillis())));
  }

  public static File createInternalPictureFile(Context context, String name) {
    Preconditions.checkNotNull(context, "context == null");
    final File d = new File(context.getFilesDir(), DIRECTORY_PICTURES);
    if (!d.exists()) {
      d.mkdirs();
    }
    return new File(d, formatName(name));
  }

  private Files() {
    throw new AssertionError("Cannot be instantiated");
  }
}
