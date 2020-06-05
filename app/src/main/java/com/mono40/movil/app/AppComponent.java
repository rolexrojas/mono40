package com.mono40.movil.app;

import com.mono40.movil.api.ApiModule;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.company.CompanyModule;
import com.mono40.movil.d.ui.main.purchase.PurchaseModule;
import com.mono40.movil.d.ui.main.recipient.index.category.SecondActivity;
import com.mono40.movil.d.ui.qr.MyQrFragment;
import com.mono40.movil.d.ui.qr.QrScannerFragment;
import com.mono40.movil.d.ui.qr.ScannedQrFragment;
import com.mono40.movil.data.DataModule;
import com.mono40.movil.gson.GsonModule;
import com.mono40.movil.insurance.micro.MicroInsuranceModule;
import com.mono40.movil.job.JobModule;
import com.mono40.movil.net.NetModule;
import com.mono40.movil.paypal.PayPalModule;
import com.mono40.movil.product.ProductModule;
import com.mono40.movil.session.SessionModule;
import com.mono40.movil.session.UpdateUserCarrierJob;
import com.mono40.movil.session.UpdateUserNameJob;
import com.mono40.movil.session.UpdateUserPictureJob;
import com.mono40.movil.store.DiskStoreModule;
import com.mono40.movil.time.TimeModule;
import com.mono40.movil.util.LogoutTimerServiceModule;

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
        com.mono40.movil.dep.AppModule.class,
        LogoutTimerServiceModule.class,
        LogoutTimerServiceModule.class,
})
public interface AppComponent extends com.mono40.movil.dep.AppComponent {

    void inject(UpdateUserNameJob job);

    void inject(UpdateUserPictureJob job);

    void inject(UpdateUserCarrierJob job);

    void inject(MyQrFragment fragment);

    void inject(QrScannerFragment fragment);

    void inject(ScannedQrFragment fragment);
}
