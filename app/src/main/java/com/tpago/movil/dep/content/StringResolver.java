package com.tpago.movil.dep.content;

import android.content.Context;

import com.tpago.movil.dep.Preconditions;

/**
 * @author hecvasro
 */
@Deprecated
public final class StringResolver {
  private final Context context;

  public StringResolver(Context context) {
    this.context = Preconditions.assertNotNull(context, "context == null");
  }

  public final String resolve(int id) {
    return context.getString(id);
  }
}
