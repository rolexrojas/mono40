package com.tpago.movil.d.ui.main.recipient.index.category.selectbank;

import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.d.ui.DepActivityModule;
import com.tpago.movil.dep.AppComponent;

import dagger.Component;

@FragmentScope@Component(
    dependencies = AppComponent.class,
    modules = {
        ActivityModule.class,
        com.tpago.movil.dep.ActivityModule.class,
        DepActivityModule.class,
        BankSelectModule.class
    }
)
public interface BankSelectComponent {
  void inject(BankListFragment fragment);
}
