package com.mono40.movil.app.ui.main.transaction.insurance.micro.index;

import com.mono40.movil.api.Api;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.insurance.micro.MicroInsurancePartnerStore;
import com.mono40.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

@Module
public final class MicroInsuranceIndexModule {

  static MicroInsuranceIndexModule create(MicroInsuranceIndexPresentation presentation) {
    return new MicroInsuranceIndexModule(presentation);
  }

  private final MicroInsuranceIndexPresentation presentation;

  private MicroInsuranceIndexModule(MicroInsuranceIndexPresentation presentation) {
    this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
  }

  @Provides
  @FragmentScope
  MicroInsuranceIndexPresenter presenter(
    MicroInsurancePartnerStore store,
    Api api,
    StringMapper stringMapper,
    AlertManager alertManager,
    TakeoverLoader takeoverLoader
  ) {
    return MicroInsuranceIndexPresenter.builder()
      .store(store)
      .api(api)
      .stringMapper(stringMapper)
      .alertManager(alertManager)
      .takeoverLoader(takeoverLoader)
      .presentation(this.presentation)
      .build();
  }
}
