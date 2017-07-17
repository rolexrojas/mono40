package com.tpago.movil.io;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.io.File;

import static com.tpago.movil.util.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
public final class Files {
  private static final String FORMAT_FILE_NAME_PICTURE = "%1$s.png";

  private static final String NAME_DIRECTORY_PICTURES
    = Environment.DIRECTORY_PICTURES;
  private static final String NAME_PROVIDER_FILE
    = String.format("%1$s.provider", BuildConfig.APPLICATION_ID);

  private static String formatName(String name) {
    return String.format(FORMAT_FILE_NAME_PICTURE, name);
  }

  public static File createExternalPictureFile(Context context) throws IllegalStateException {
    assertNotNull(context, "context == null");
    final File d = context.getExternalFilesDir(NAME_DIRECTORY_PICTURES);
    if (Objects.checkIfNull(d)) {
      throw new IllegalStateException("context.getExternalFilesDir(NAME_DIRECTORY_PICTURES) == null");
    }
    if (!d.exists()) {
      d.mkdirs();
    }
    return new File(d, formatName(Long.toString(System.currentTimeMillis())));
  }

  public static File createInternalCacheDirectory(Context context) {
    return assertNotNull(context, "context == null").getCacheDir();
  }

  public static File createInternalPictureFile(Context context, String name) {
    assertNotNull(context, "context == null");
    final File d = new File(context.getFilesDir(), NAME_DIRECTORY_PICTURES);
    if (!d.exists()) {
      d.mkdirs();
    }
    return new File(d, formatName(name));
  }

  public static Uri getFileUri(File file) {
    return Uri.fromFile(Preconditions.assertNotNull(file, "file == null"));
  }

  public static Uri getFileUri(Context context, File file) {
    return FileProvider.getUriForFile(
      assertNotNull(context, "context == null"),
      NAME_PROVIDER_FILE,
      assertNotNull(file, "file == null"));
  }

  private Files() {
    throw new AssertionError("Cannot be instantiated");
  }
}
