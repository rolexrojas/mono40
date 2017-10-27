package com.tpago.movil.d.ui.main.recipient.addition;

import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.d.domain.RecipientManager;

import com.tpago.movil.d.ui.main.recipient.index.category.Category;

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
  AddRecipientPresenter providePresenter(RecipientManager recipientManager) {
    return new AddRecipientPresenter(recipientManager);
  }
}
