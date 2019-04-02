package com.tpago.movil.d.ui.main.recipient.index.category;

import android.support.v4.app.FragmentManager;

import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.app.ui.activity.base.ActivityBase;
import com.tpago.movil.app.ui.alert.ModuleAlert;
import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.app.ui.loader.takeover.ModuleLoaderTakeover;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.ui.DepActivityBase;
import com.tpago.movil.dep.ActivityModule;
import com.tpago.movil.dep.User;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.paypal.PayPalAccountStore;
import com.tpago.movil.util.ObjectHelper;

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
