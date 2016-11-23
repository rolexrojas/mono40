package com.gbh.movil.data;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class UriUtils {
  private UriUtils() {
  }

  /**
   * TODO
   *
   * @param uri
   *   TODO
   *
   * @return TODO
   */
  public static Uri getUriOrEmpty(@Nullable Uri uri) {
    return Utils.isNotNull(uri) ? uri : Uri.EMPTY;
  }
}
