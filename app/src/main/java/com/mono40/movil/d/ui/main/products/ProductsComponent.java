package com.mono40.movil.d.ui.main.products;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.d.ui.main.DepMainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(
  modules = ProductsModule.class,
  dependencies = DepMainComponent.class
)
public interface ProductsComponent {

  void inject(ProductsFragment fragment);
}
