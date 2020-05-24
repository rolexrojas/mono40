package com.mono40.movil.util;

import android.content.Context;

import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.fragment.base.BaseFragmentModule;
import com.mono40.movil.app.ui.main.settings.help.FragmentHelp;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.partner.PartnerStore;
import com.mono40.movil.dep.AppComponent;
import com.mono40.movil.dep.User;
import com.mono40.movil.app.ui.main.settings.profile.ProfileComponent;
import com.mono40.movil.app.ui.main.settings.profile.ProfileModule;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.data.SchedulerProvider;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.d.domain.BalanceManager;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.domain.pos.PosBridge;
import com.mono40.movil.d.domain.util.EventBus;
import com.mono40.movil.d.domain.RecipientManager;
import com.mono40.movil.d.ui.ActivityComponent;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.dep.net.NetworkService;
import com.mono40.movil.paypal.PayPalAccountStore;
import com.mono40.movil.session.SessionManager;

import dagger.Component;
import dagger.Module;
import dagger.Provides;


@Component(modules = LogoutTimerServiceModule.class,
dependencies = AppComponent.class)
public interface LogoutTimerServiceComponent {


    SessionManager sessionManager();
}
