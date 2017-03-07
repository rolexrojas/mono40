package com.tpago.movil.dep.ui.main.products;

import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.dep.ui.main.DepMainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = ProductsModule.class, dependencies = DepMainComponent.class)
interface ProductsComponent {
  void inject(ProductsFragment fragment);
}
