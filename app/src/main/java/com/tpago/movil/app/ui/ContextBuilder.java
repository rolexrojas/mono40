package com.tpago.movil.app.ui;

import android.content.Context;

import com.tpago.movil.util.ObjectHelper;

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
  private final List<WrapperFunction> wrapperFunctionList = new ArrayList<>();

  private ContextBuilder(Context context) {
    this.context = ObjectHelper.checkNotNull(context, "context");
  }

  final ContextBuilder wrapperFunction(WrapperFunction wrapperFunction) {
    this.wrapperFunctionList.add(ObjectHelper.checkNotNull(wrapperFunction, "wrapperFunction"));
    return this;
  }

  final Context build() {
    Context wrappedContext = this.context;
    for (WrapperFunction wrapperFunction : this.wrapperFunctionList) {
      wrappedContext = wrapperFunction.apply(wrappedContext);
    }
    return wrappedContext;
  }

  interface WrapperFunction {

    Context apply(Context context);
  }
}
