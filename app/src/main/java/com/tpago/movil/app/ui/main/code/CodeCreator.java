package com.tpago.movil.app.ui.main.code;

import android.support.v4.app.FragmentManager;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.fragment.FragmentHelper;
import com.tpago.movil.Code;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.app.ui.main.transaction.disburse.index.FragmentDisburseIndex;
import com.tpago.movil.util.function.Consumer;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public final class CodeCreator {

    private static final String TAG = CodeCreator.class.getCanonicalName();

    public static CodeCreator create(FragmentManager fragmentManager) {
        return new CodeCreator(fragmentManager);
    }

    private final FragmentManager fragmentManager;
    FragmentReplacer fragmentReplacer;

    private Request activeRequest;

    private CodeCreator(FragmentManager fragmentManager) {
        this.fragmentManager = ObjectHelper.checkNotNull(fragmentManager, "fragmentManager");
        fragmentReplacer = FragmentReplacer.create(fragmentManager, R.id.containerFrameLayout);
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

        this.fragmentReplacer.begin(CodeCreatorDialogFragment.create(), TAG)
                .transition(FragmentReplacer.Transition.FIFO)
                .addToBackStack()
                .commit();
    }

    public enum RequestType {
        PIN,
        SESSION_OPENING_METHOD
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
