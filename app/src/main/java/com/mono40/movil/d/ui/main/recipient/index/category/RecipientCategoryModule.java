package com.mono40.movil.d.ui.main.recipient.index.category;

import androidx.fragment.app.FragmentManager;

import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.activity.base.ActivityBase;
import com.mono40.movil.app.ui.alert.ModuleAlert;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.app.ui.loader.takeover.ModuleLoaderTakeover;
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoader;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.domain.RecipientManager;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.ui.DepActivityBase;
import com.mono40.movil.dep.ActivityModule;
import com.mono40.movil.dep.User;
import com.mono40.movil.dep.net.NetworkService;
import com.mono40.movil.paypal.PayPalAccountStore;
import com.mono40.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module(includes = {
        ActivityModule.class
})
class RecipientCategoryModule {

    private final Category category;
    private final DepActivityBase activity;

    @Provides
    @ActivityScope
    DepActivityBase provideActivity() {
        return activity;
    }

    @Provides
    @FragmentScope
    TakeoverLoader takeoverLoader() {
        return TakeoverLoader.create(this.fragmentManager());
    }

    RecipientCategoryModule(Category category, DepActivityBase activity) {
        this.category = category;
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    FragmentManager fragmentManager() {
        return this.activity.getSupportFragmentManager();
    }

    @Provides
    @FragmentScope
    Category category() {
        return this.category;
    }

    @Provides
    @FragmentScope
    RecipientCategoryPresenter providePresenter(
            StringHelper stringHelper,
            RecipientManager recipientManager,
            User user,
            NetworkService networkService,
            DepApiBridge depApiBridge,
            PayPalAccountStore payPalAccountStore
    ) {
        return new RecipientCategoryPresenter(
                user,
                recipientManager,
                depApiBridge,
                this.category,
                stringHelper,
                networkService,
                payPalAccountStore
        );
    }
}
