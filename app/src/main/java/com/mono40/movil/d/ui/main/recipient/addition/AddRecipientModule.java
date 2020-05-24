package com.mono40.movil.d.ui.main.recipient.addition;

import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.d.domain.RecipientManager;

import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.ui.main.recipient.index.category.Category;
import com.mono40.movil.app.StringMapper;

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
