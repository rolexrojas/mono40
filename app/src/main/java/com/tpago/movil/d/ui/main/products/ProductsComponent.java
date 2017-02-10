package com.tpago.movil.d.ui.main.products;

import com.tpago.movil.d.ui.FragmentScope;
import com.tpago.movil.d.ui.main.MainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = ProductsModule.class, dependencies = MainComponent.class)
interface ProductsComponent {
  void inject(ProductsFragment fragment);
}
