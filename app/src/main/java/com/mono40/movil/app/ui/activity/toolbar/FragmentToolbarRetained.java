package com.mono40.movil.app.ui.activity.toolbar;

import androidx.fragment.app.Fragment;

import com.mono40.movil.app.di.ComponentBuilderSupplier;
import com.mono40.movil.app.di.ComponentBuilderSupplierContainer;
import com.mono40.movil.app.ui.fragment.FragmentQualifier;

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
