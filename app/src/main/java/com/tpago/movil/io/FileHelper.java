package com.tpago.movil.io;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.util.ObjectHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class FileHelper {

  private static final String DIRECTORY_NAME_PICTURES = Environment.DIRECTORY_PICTURES;
  private static final String FILE_NAME_FORMAT_PICTURE = "%1$s.png";

  private static final String PROVIDER_NAME_FILE = BuildConfig.APPLICATION_ID + ".provider";

  public static File createInternalCacheDirectory(Context context) {
    return ObjectHelper.checkNotNull(context, "context")
      .getCacheDir();
  }

  private static String generateRandomFileNamePrefix() {
    return UUID.randomUUID()
      .toString();
  }

  private static String generateRandomPictureFileName() {
    return String.format(FILE_NAME_FORMAT_PICTURE, generateRandomFileNamePrefix());
  }

  public static File createExternalPictureFile(Context context) throws IllegalStateException {
    ObjectHelper.checkNotNull(context, "context");

    final File directory = context.getExternalFilesDir(DIRECTORY_NAME_PICTURES);
    if (ObjectHelper.isNull(directory)) {
      throw new IllegalStateException(
        String.format(
          "ObjectHelper.isNull(context.getExternalFilesDir(\"%1$s\"))",
          DIRECTORY_NAME_PICTURES
        )
      );
    }
    if (!directory.exists()) {
      directory.mkdirs();
    }
    return new File(directory, generateRandomPictureFileName());
  }

  private static File createInternalPictureDirectory(Context context) {
    ObjectHelper.checkNotNull(context, "context");
    final File directory = new File(context.getFilesDir(), DIRECTORY_NAME_PICTURES);
    if (!directory.exists()) {
      Timber.d("\"%1$s\".mkdirs()=%2$s", directory.getAbsolutePath(), directory.mkdirs());
    }
    return directory;
  }

  public static File createInternalPictureFile(Context context) {
    return new File(createInternalPictureDirectory(context), generateRandomPictureFileName());
  }

  public static File createInternalPictureFileCopy(Context context, File input) throws IOException {
    ObjectHelper.checkNotNull(context, "context");
    ObjectHelper.checkNotNull(input, "input");
    final InputStream inputStream = new FileInputStream(input);
    final File output = createInternalPictureFile(context);
    final OutputStream outputStream = new FileOutputStream(output);
    final byte[] buffer = new byte[1024]; // TODO: Learn why is 1024 used.
    int length;
    while ((length = inputStream.read(buffer)) > 0) {
      outputStream.write(buffer, 0, length);
    }
    outputStream.close();
    inputStream.close();
    return output;
  }

  public static Uri getFileUri(File file) {
    return Uri.fromFile(ObjectHelper.checkNotNull(file, "file"));
  }

  public static Uri getFileUri(Context context, File file) {
    return FileProvider.getUriForFile(
      ObjectHelper.checkNotNull(context, "context"),
      PROVIDER_NAME_FILE,
      ObjectHelper.checkNotNull(file, "file")
    );
  }

  public static void deleteFile(File file) {
    ObjectHelper.checkNotNull(file, "file");
    if (file.exists()) {
      Timber.d("deleteFile(\"%1$s\")=%2$s", file.getAbsoluteFile(), file.delete());
    }
  }

  private FileHelper() {
  }
}
