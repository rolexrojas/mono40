package com.tpago.movil.d.ui.main.products;

import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.d.ui.main.DepMainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = ProductsModule.class, dependencies = DepMainComponent.class)
interface ProductsComponent {
  void inject(ProductsFragment fragment);
}
