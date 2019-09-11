package com.tpago.movil.app.ui.activity.toolbar;

import androidx.fragment.app.Fragment;

import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.app.di.ComponentBuilderSupplierContainer;
import com.tpago.movil.app.ui.fragment.FragmentQualifier;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public abstract class FragmentToolbarRetained extends Fragment implements
  ComponentBuilderSupplierContainer {

  @Inject
  @FragmentQualifier
  protected ComponentBuilderSupplier componentBuilderSupplier;

  @Override
  public ComponentBuilderSupplier componentBuilderSupplier() {
    return this.componentBuilderSupplier;
  }
}
