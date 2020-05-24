package com.mono40.movil.d.ui.main.recipient.index.category.selectbank;

import com.mono40.movil.app.ui.activity.base.ActivityModule;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.d.ui.DepActivityModule;
import com.mono40.movil.dep.AppComponent;

import dagger.Component;

@FragmentScope@Component(
    dependencies = AppComponent.class,
    modules = {
        ActivityModule.class,
        com.mono40.movil.dep.ActivityModule.class,
        DepActivityModule.class,
        BankSelectModule.class
    }
)
public interface BankSelectComponent {
  void inject(BankListFragment fragment);
}
