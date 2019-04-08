package com.tpago.movil.d.ui.main.products;

import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.d.ui.main.DepMainComponent;

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
