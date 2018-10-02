package com.tpago.movil.app.ui.init.unlock.ChangePassword;

import com.tpago.movil.app.ui.Presenter;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;

/**
 * Created by solucionesgbh on 6/12/18.
 */

public class ChangePasswordPresenter   extends Presenter<ChangePasswordPresentation> {
	static ChangePasswordPresenter.Builder builder() {
		return new ChangePasswordPresenter.Builder();
	}

	private ChangePasswordPresenter(ChangePasswordPresenter.Builder builder) {
		super(builder.presentation);
	}

	static final class Builder {
		private ChangePasswordPresentation presentation;

		private Builder() {
		}

		final ChangePasswordPresenter.Builder presentation(ChangePasswordPresentation presentation) {
			this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
			return this;
		}

		final ChangePasswordPresenter build() {
			BuilderChecker.create()
				.addPropertyNameIfMissing("presentation", ObjectHelper.isNull(this.presentation))
				.checkNoMissingProperties();

			return new ChangePasswordPresenter(this);
		}
	}
}