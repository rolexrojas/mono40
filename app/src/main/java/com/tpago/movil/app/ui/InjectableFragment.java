package com.tpago.movil.app.ui;

import android.content.Context;

import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.app.di.ComponentBuilderSupplierContainer;
import com.tpago.movil.app.ui.fragment.base.FragmentBase;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class InjectableFragment extends FragmentBase {

  protected ComponentBuilderSupplier parentComponentBuilderSupplier;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    if (!(context instanceof ComponentBuilderSupplierContainer)) {
      throw new ClassCastException("!(context instanceof ComponentBuilderSupplierContainer)");
    }
    this.parentComponentBuilderSupplier = ((ComponentBuilderSupplierContainer) context)
      .componentBuilderSupplier();
  }

  @Override
  public void onDetach() {
    this.parentComponentBuilderSupplier = null;

    super.onDetach();
  }
}
