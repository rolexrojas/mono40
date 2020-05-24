package com.mono40.movil.d.ui.main.recipient.addition;

import android.content.Context;

import com.mono40.movil.app.ui.activity.base.ActivityModule;
import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.bank.BankStore;
import com.mono40.movil.company.partner.PartnerStore;
import com.mono40.movil.dep.AppComponent;
import com.mono40.movil.d.data.SchedulerProvider;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.ui.ActivityComponent;
import com.mono40.movil.d.ui.DepActivityModule;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(
  dependencies = AppComponent.class,
  modules = {
    ActivityModule.class,
    com.mono40.movil.dep.ActivityModule.class,
    DepActivityModule.class,
    AddRecipientModule.class
  }
)
public interface AddRecipientComponent extends ActivityComponent {

  void inject(AddRecipientActivityBase activity);

  void inject(SearchOrChooseRecipientFragment fragment);

  void inject(RecipientBuilderFragment fragment);

  CompanyHelper logoFactory();

  BankStore bankStore();

  PartnerStore partnerStore();

  Context provideContext();

  DepApiBridge provideApiBridge();

  SchedulerProvider provideSchedulerProvider();
}
