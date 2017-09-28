package com.tpago.movil.d.ui.main.recipient.addition;

import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.session.SessionManager;

import com.tpago.movil.d.ui.main.recipient.index.category.Category;
import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
class AddRecipientModule {

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
    SessionManager sessionManager,
    RecipientManager recipientManager
  ) {
    return new AddRecipientPresenter(
      sessionManager.getSession()
        .getAuthToken(),
      recipientManager,
      this.category
    );
  }
}
