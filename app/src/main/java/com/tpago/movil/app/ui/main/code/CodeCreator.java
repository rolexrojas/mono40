package com.tpago.movil.app.ui.main.code;

import android.support.v4.app.FragmentManager;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.app.ui.FragmentHelper;
import com.tpago.movil.domain.Code;
import com.tpago.movil.util.ObjectHelper;

import java.util.function.Consumer;

/**
 * @author hecvasro
 */
public final class CodeCreator {

  private static final String TAG = CodeCreator.class.getCanonicalName();

  public static CodeCreator create(FragmentManager fragmentManager) {
    return new CodeCreator(fragmentManager);
  }

  private final FragmentManager fragmentManager;

  private Request activeRequest;

  private CodeCreator(FragmentManager fragmentManager) {
    this.fragmentManager = ObjectHelper.checkNotNull(fragmentManager, "fragmentManager");
  }

  private void checkActive() {
    if (ObjectHelper.isNull(this.activeRequest)) {
      throw new IllegalStateException("ObjectHelper.isNull(this.activeRequest)");
    }
  }

  final RequestType activeRequestType() {
    this.checkActive();

    return this.activeRequest.type();
  }

  final void resolveActiveRequest(Code code) {
    this.checkActive();

    this.activeRequest.consumer()
      .accept(code);
    this.activeRequest = null;

    FragmentHelper.dismissByTag(this.fragmentManager, TAG);
  }

  public final void create(RequestType requestType, Consumer<Code> codeConsumer) {
    ObjectHelper.checkNotNull(requestType, "requestType");
    ObjectHelper.checkNotNull(codeConsumer, "codeConsumer");

    if (ObjectHelper.isNotNull(this.activeRequest)) {
      FragmentHelper.dismissByTag(this.fragmentManager, TAG);

      this.activeRequest = null;
    }

    this.activeRequest = Request.create(requestType, codeConsumer);

    CodeCreatorDialogFragment.create()
      .show(this.fragmentManager, TAG);
  }

  public enum RequestType {
    PIN,
    ALTAUTH
  }

  @AutoValue
  static abstract class Request {

    static Request create(RequestType type, Consumer<Code> consumer) {
      return new AutoValue_CodeCreator_Request(type, consumer);
    }

    Request() {
    }

    abstract RequestType type();

    abstract Consumer<Code> consumer();

    @Memoized
    @Override
    public abstract String toString();

    @Memoized
    @Override
    public abstract int hashCode();
  }
}
