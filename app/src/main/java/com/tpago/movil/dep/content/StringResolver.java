package com.tpago.movil.dep.content;

import android.content.Context;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
public final class StringResolver {

  private final Context context;

  public StringResolver(Context context) {
    this.context = ObjectHelper.checkNotNull(context, "context");
  }

  public final String resolve(int id) {
    return context.getString(id);
  }
}
