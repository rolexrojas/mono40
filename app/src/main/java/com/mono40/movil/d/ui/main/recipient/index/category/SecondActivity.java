package com.mono40.movil.d.ui.main.recipient.index.category;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mono40.movil.BuildConfig;
import com.mono40.movil.R;
import com.mono40.movil.ServiceInformation.Maintenance;
import com.mono40.movil.api.ApiModuleFlavored;
import com.mono40.movil.api.ApiRetrofit;
import com.mono40.movil.api.ApiSecretTokenResponse;
import com.mono40.movil.app.ui.activity.base.ActivityBase;
import com.mono40.movil.d.data.api.CustomerSecretTokenResponse;
import com.mono40.movil.d.domain.api.ApiResult;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.ui.main.transaction.TransactionCreationComponent;
import com.mono40.movil.d.ui.main.transaction.products.LoanTransactionCreationPresenter;
import com.mono40.movil.util.ObjectHelper;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondActivity extends ActivityBase{

    @BindView(R.id.radioButton)
    RadioButton oilChangeRadiobutton;

    @BindView(R.id.radioButton2)
    RadioButton oilFilterChangeRadiobutton;

    @BindView(R.id.radioButton3)
    RadioButton transmissionFluidRadiobutton;

    @BindView(R.id.radioButton4)
    RadioButton brakeOilRadioButton;

    @BindView(R.id.radioButton5)
    RadioButton oilHydraulicRadiobutton;

    @BindView(R.id.radioButton6)
    RadioButton oilAirConditionRadiobutton;

    @BindView(R.id.radioButton7)
    RadioButton waterWiperRadiobutton;

    @BindView(R.id.radioButton8)
    RadioButton batteryWaterRadiobutton;

    @BindView(R.id.radioButton9)
    RadioButton radiatorHosesRadiobutton;

    @BindView(R.id.radioButton10)
    RadioButton warmerHosesRadioButton;

    @BindView(R.id.radioButton11)
    RadioButton airConditionerHoseRadioButton;

    @BindView(R.id.radioButton12)
    RadioButton airConditionerFilterRadioButton;

    @BindView(R.id.radioButton13)
    RadioButton tireAirPressureRadioButton;

    @BindView(R.id.radioButton14)
    RadioButton tiresUseMillageRadiobutton;

    @BindView(R.id.radioButton15)
    RadioButton wiperWindsShieldRadiobutton;

    @BindView(R.id.radioButton16)
    RadioButton frontLightsRadiobutton;

    @BindView(R.id.radioButton17)
    RadioButton seatBeltRadiobutton;

    @BindView(R.id.radioButton18)
    RadioButton breakRadiobutton;


   /* private final DepApiBridge depApiBridge;
    private final Context context;
    private final Gson gson; */

   /* public SecondActivity(DepApiBridge depApiBridge, Context context, Gson gson) {
        this.depApiBridge = depApiBridge;
        this.context = context;
        this.gson = gson;
    } */

    @Override
    protected int layoutResId() {
        return R.layout.second_check_activity;
    }



    public void finishCheck(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        final ApiRetrofit api = retrofit.create(ApiRetrofit.class);

        Bundle extras = this.getIntent().getExtras();

        String insurance = extras.getString(RecipientCategoryFragment.EXTRA_INSURANCE, "DEFAULT INSURANCE");
        String millage = extras.getString(RecipientCategoryFragment.EXTRA_MILLAGE, "DEFAULT MILLGAGE");
        String year = extras.getString(RecipientCategoryFragment.EXTRA_YEAR, "DEFAULT YEAR");
        String model = extras.getString(RecipientCategoryFragment.EXTRA_MODEL, "DEFAULT MODEL");
        String make = extras.getString(RecipientCategoryFragment.EXTRA_MAKE, "DEFAULT MAKE");
       // DepApiBridge depApiBridge = extras.getParcelable("depApiBridgeP");
        String message = "Finish " +
                " insurance=" + insurance +
                " millage=" + millage +
                " year=" + year +
                " model=" + model +
                " make=" + make;

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        Maintenance maintenance = new Maintenance();
        maintenance.setOilChange(true);
        maintenance.setOilFilterChange(true);
        maintenance.setTransmissionFluidChange(true);
        maintenance.setSteeringFluidChange(true);
        maintenance.setCoolantFluidChange(true);
        maintenance.setWipeWaterCheck(true);
        maintenance.setBatteryWaterChange(true);
        maintenance.setRadiatorHosesCheck(true);
        maintenance.setHeaterHosesCheck(false);
        maintenance.setAirCondHosesCheck(false);
        maintenance.setAirFilterChange(false);
        maintenance.setTirePressureCheck(false);
        maintenance.setTireWearCheck(true);
        maintenance.setWipersCheck(true);
        maintenance.setHeadLampAlignmentCheck(false);
        maintenance.setSeatBeltCheck(false);
        maintenance.setParkingBreakCheck(false);

        //Single<Result<ApiSecretTokenResponse>> encryptedMaintenance =
        Single<Response<ApiSecretTokenResponse>> encryptedMaintenance = api.getEncryptedMaintenance(insurance, model, make, year, millage, maintenance);
         Toast.makeText(this, encryptedMaintenance.toString(), Toast.LENGTH_LONG).show();

    }

    private void handleCustomerSecretResult() {
     //   this.takeoverLoader.hide();
    }

    private void handleCustomerSecretKeyResult(ApiResult<CustomerSecretTokenResponse> customerSecretKeyResponseApiResult) {
        if (customerSecretKeyResponseApiResult.isSuccessful()) {
           // sessionManager.saveCustomerSecretKey(customerSecretKeyResponseApiResult.getData().key());
            Toast.makeText(this, customerSecretKeyResponseApiResult.getData().toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(Throwable throwable) {
       // Timber.e(throwable, "Could get Data from Customer");
     //   this.alertManager.showAlertForGenericFailure();
    }

    private void showTakeoverLoader(Disposable disposable) {
      //  this.takeoverLoader.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
