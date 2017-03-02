package com.tpago.movil.dep.ui.main.transactions.bills;

import com.tpago.movil.app.Presenter;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.ui.main.transactions.TransactionCreationComponent;
import com.tpago.movil.util.Preconditions;

import javax.inject.Inject;

/**
 * @author hecvasro
 */

public class BillTransactionCreationPresenter
  extends Presenter<BillTransactionCreationPresenter.View> {
  @Inject Recipient recipient;

  public BillTransactionCreationPresenter(View view, TransactionCreationComponent component) {
    super(view);
    // Injects all the annotated dependencies.
    Preconditions.checkNotNull(component, "component == null")
      .inject(this);
  }

  public interface View extends Presenter.View {
  }
}
