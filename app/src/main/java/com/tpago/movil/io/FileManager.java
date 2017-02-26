package com.tpago.movil.io;

import android.content.Context;

import com.tpago.movil.util.Preconditions;

import java.io.File;

/**
 * @author hecvasro
 */
public final class FileManager {
  private final Context context;

  public FileManager(Context context) {
    this.context = Preconditions.checkNotNull(context, "context == null");
  }

  public final File openFile(String name) {
    return new File(context.getFilesDir(), name);
  }

  public final File openFile() {
    return openFile(Long.toString(System.currentTimeMillis()));
  }
}
