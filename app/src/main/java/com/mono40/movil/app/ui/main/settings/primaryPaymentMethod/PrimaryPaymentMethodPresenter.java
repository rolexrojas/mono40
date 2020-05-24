package com.mono40.movil.app.ui.main.settings.primaryPaymentMethod;

import com.mono40.movil.app.ui.FragmentActivityComponent;
import com.mono40.movil.app.ui.Presenter;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.reactivex.DisposableUtil;
import com.mono40.movil.util.BuilderChecker;
import com.mono40.movil.util.ObjectHelper;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

/**
 * Created by solucionesgbh on 5/30/18.
 */

public class PrimaryPaymentMethodPresenter  extends Presenter<PrimaryPaymentMethodPresentation> {

    static PrimaryPaymentMethodPresenter.Builder builder() {
        return new PrimaryPaymentMethodPresenter.Builder();
    }
    static PrimaryPaymentMethodPresenter create(
            PrimaryPaymentMethodPresentation presentation,
            FragmentActivityComponent component
    ) {
        return new PrimaryPaymentMethodPresenter(presentation, component);
    }

    private Disposable disposable = Disposables.disposed();

    @Inject
    ProductManager productManager;
    private PrimaryPaymentMethodPresenter(
            PrimaryPaymentMethodPresentation presentation,
            FragmentActivityComponent component
    ) {
        super(presentation);

        // Injects all annotated dependencies.
        component.inject(this);
    }

    private PrimaryPaymentMethodPresenter(PrimaryPaymentMethodPresenter.Builder builder) {
        super(builder.presentation);

    }

    @Override
    public void onPresentationResumed() {
    }

    @Override
    public void onPresentationPaused() {
        DisposableUtil.dispose(this.disposable);

        super.onPresentationPaused();
    }

    static final class Builder {

        private PrimaryPaymentMethodPresentation presentation;

        private Builder() {
        }

        final PrimaryPaymentMethodPresenter.Builder presentation(PrimaryPaymentMethodPresentation presentation) {
            this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
            return this;
        }

        final PrimaryPaymentMethodPresenter build() {
            BuilderChecker.create()
                    .addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
                    .checkNoMissingProperties();

            return new PrimaryPaymentMethodPresenter(this);
        }
    }
}
