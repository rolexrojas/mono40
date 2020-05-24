package com.mono40.movil.app.ui.activity.base;

import android.content.Context;

import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.function.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hector Vasquez
 */
final class ContextBuilder {

  static ContextBuilder create(Context context) {
    return new ContextBuilder(context);
  }

  private final Context context;
  private final List<Function<Context, Context>> functions = new ArrayList<>();

  private ContextBuilder(Context context) {
    this.context = ObjectHelper.checkNotNull(context, "context");
  }

  final ContextBuilder function(Function<Context, Context> function) {
    this.functions.add(ObjectHelper.checkNotNull(function, "function"));
    return this;
  }

  final Context build() {
    Context context = this.context;
    for (Function<Context, Context> function : this.functions) {
      context = function.apply(context);
    }
    return context;
  }
}
