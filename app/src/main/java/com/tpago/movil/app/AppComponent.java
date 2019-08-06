package com.tpago.movil.app;

import com.tpago.movil.api.ApiModule;
import com.tpago.movil.company.CompanyModule;
import com.tpago.movil.d.ui.qr.MyQrFragment;
import com.tpago.movil.d.ui.qr.QrScannerFragment;
import com.tpago.movil.data.DataModule;
import com.tpago.movil.gson.GsonModule;
import com.tpago.movil.insurance.micro.MicroInsuranceModule;
import com.tpago.movil.job.JobModule;
import com.tpago.movil.net.NetModule;
import com.tpago.movil.paypal.PayPalModule;
import com.tpago.movil.product.ProductModule;
import com.tpago.movil.session.SessionModule;
import com.tpago.movil.session.UpdateUserCarrierJob;
import com.tpago.movil.session.UpdateUserNameJob;
import com.tpago.movil.session.UpdateUserPictureJob;
import com.tpago.movil.store.DiskStoreModule;
import com.tpago.movil.time.TimeModule;
import com.tpago.movil.util.LogoutTimerServiceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApiModule.class,
        AppComponentBuilderModule.class,
        AppModule.class,
        CompanyModule.class,
        DataModule.class,
        DiskStoreModule.class,
        GsonModule.class,
        JobModule.class,
        MicroInsuranceModule.class,
        NetModule.class,
        PayPalModule.class,
        ProductModule.class,
        SessionModule.class,
        TimeModule.class,
        com.tpago.movil.dep.AppModule.class,
        LogoutTimerServiceModule.class
})
public interface AppComponent extends com.tpago.movil.dep.AppComponent {

    void inject(UpdateUserNameJob job);

    void inject(UpdateUserPictureJob job);

    void inject(UpdateUserCarrierJob job);

    void inject(MyQrFragment fragment);

    void inject(QrScannerFragment fragment);
}
