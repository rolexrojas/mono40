package com.tpago.movil.d.ui.main.recipient.addition;

import android.content.Context;

import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.bank.BankStore;
import com.tpago.movil.company.partner.PartnerStore;
import com.tpago.movil.dep.AppComponent;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.ui.ActivityComponent;
import com.tpago.movil.d.ui.DepActivityModule;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(
  dependencies = AppComponent.class,
  modules = {
    ActivityModule.class,
    com.tpago.movil.dep.ActivityModule.class,
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
