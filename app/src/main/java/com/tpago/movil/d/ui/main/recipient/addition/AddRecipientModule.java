package com.tpago.movil.d.ui.main.recipient.addition;

import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.d.domain.RecipientManager;

import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.ui.main.recipient.index.category.Category;
import com.tpago.movil.data.StringMapper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
final class AddRecipientModule {

  private final Category category;

  AddRecipientModule(Category category) {
    this.category = category;
  }

  @Provides
  @ActivityScope
  Category category() {
    return this.category;
  }

  @Provides
  @ActivityScope
  AddRecipientPresenter providePresenter(
    RecipientManager recipientManager,
    DepApiBridge apiBridge,
    AlertManager alertManager,
    StringMapper stringMapper,
    TakeoverLoader takeoverLoader
  ) {
    return new AddRecipientPresenter(
      recipientManager,
      apiBridge,
      alertManager,
      stringMapper,
      takeoverLoader
    );
  }
}
