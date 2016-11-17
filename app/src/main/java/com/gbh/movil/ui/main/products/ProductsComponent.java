package com.gbh.movil.ui.main.products;

import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.main.MainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = ProductsModule.class, dependencies = MainComponent.class)
interface ProductsComponent {
  void inject(ProductsFragment fragment);
}
