package com.mono40.movil.io;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import androidx.core.content.FileProvider;

import com.mono40.movil.BuildConfig;
import com.mono40.movil.util.ObjectHelper;

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

  private static final String DIR_NAME_PICS = Environment.DIRECTORY_PICTURES;

  private static final String FILE_NAME_FORMAT_PIC = "%1$s.png";

  private static final String PROVIDER_NAME_FILE = BuildConfig.APPLICATION_ID + ".provider";

  public static File createIntCacheDir(Context context) {
    return ObjectHelper.checkNotNull(context, "context")
      .getCacheDir();
  }

  private static String createRandomFileName() {
    return UUID.randomUUID()
      .toString();
  }

  private static String createRandPicFileName() {
    return String.format(FILE_NAME_FORMAT_PIC, createRandomFileName());
  }

  private static File createDir(File directory) {
    ObjectHelper.checkNotNull(directory, "directory");
    if (!directory.exists()) {
      Timber.d("createDir(\"%1$s\")=%2$s", directory.getAbsolutePath(), directory.mkdirs());
    }
    return directory;
  }

  public static File createExtPicDir(Context context) {
    return createDir(context.getExternalFilesDir(DIR_NAME_PICS));
  }

  public static File createExtPicFile(Context context) {
    return new File(createExtPicDir(context), createRandPicFileName());
  }

  public static File createIntPicDir(Context context) {
    return createDir(new File(context.getFilesDir(), DIR_NAME_PICS));
  }

  public static File createIntPicFile(Context context) {
    return new File(createIntPicDir(context), createRandPicFileName());
  }

  public static File createIntPicFileCopy(Context context, File input) throws IOException {
    ObjectHelper.checkNotNull(context, "context");
    ObjectHelper.checkNotNull(input, "input");
    final InputStream inputStream = new FileInputStream(input);
    final File output = createIntPicFile(context);
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

  public static Uri getFileUri(Context context, File file) {
    return FileProvider
      .getUriForFile(context, PROVIDER_NAME_FILE, file);
  }

  public static void deleteFile(File file) {
    ObjectHelper.checkNotNull(file, "file");
    if (file.exists()) {
      Timber.d("deleteFile(\"%1$s\")=%2$s", file.getAbsoluteFile(), file.delete());
    }
  }

  public static void deleteDir(File dir) {
    ObjectHelper.checkNotNull(dir, "dir");
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException("!directory.isDirectory()");
    }
    for (File file : dir.listFiles()) {
      if (file.isDirectory()) {
        deleteDir(file);
      } else {
        deleteFile(file);
      }
    }
    deleteFile(dir);
  }

  private FileHelper() {
  }
}
