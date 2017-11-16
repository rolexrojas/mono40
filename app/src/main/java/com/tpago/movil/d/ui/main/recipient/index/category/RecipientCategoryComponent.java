package com.tpago.movil.d.ui.main.recipient.index.category;

import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.d.ui.main.DepMainComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(modules = RecipientCategoryModule.class, dependencies = DepMainComponent.class)
interface RecipientCategoryComponent {

  void inject(RecipientCategoryFragment fragment);
}
