package com.mono40.movil.d.ui.main.recipient.index.category;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mono40.movil.BuildConfig;
import com.mono40.movil.R;
import com.mono40.movil.ServiceInformation.Maintenance;
import com.mono40.movil.api.ApiBanks;
import com.mono40.movil.api.ApiModuleFlavored;
import com.mono40.movil.api.ApiResponse;
import com.mono40.movil.api.ApiRetrofit;
import com.mono40.movil.api.ApiRetrofitImpl;
import com.mono40.movil.api.ApiSecretTokenResponse;
import com.mono40.movil.api.CodeForQRImage;
import com.mono40.movil.api.EmptyMapperResult;
import com.mono40.movil.api.IPService;
import com.mono40.movil.api.MapperFailureData;
import com.mono40.movil.api.MapperResult;
import com.mono40.movil.app.ui.activity.base.ActivityBase;
import com.mono40.movil.d.data.api.CustomerSecretTokenResponse;
import com.mono40.movil.d.domain.api.ApiError;
import com.mono40.movil.d.domain.api.ApiResult;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.ui.ChildFragment;
import com.mono40.movil.d.ui.main.MainContainer;
import com.mono40.movil.d.ui.main.transaction.TransactionCreationComponent;
import com.mono40.movil.d.ui.main.transaction.products.LoanTransactionCreationPresenter;
import com.mono40.movil.d.ui.qr.MyQrFragment;
import com.mono40.movil.d.ui.qr.QrActivity;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.util.FailureData;
import com.mono40.movil.util.ObjectHelper;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.Component;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
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

    @Inject
    public SessionManager sessionManager;

    public static String QR_CODE_INFORMATION = "QR_CODE_INFORMATION";

    private static final int REQUEST_CODE_TRANSACTION_CREATION = 1;

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

       // GsonBuilder builder = new GsonBuilder();
        //builder.registerTypeAdapter(CodeForQRImage.class, new CodeForQRImage());
        //Gson gson = builder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        final IPService api = retrofit.create(IPService.class);
     /*   final Converter<ResponseBody, FailureData> apiFailureDataConverter = retrofit
                .responseBodyConverter(FailureData.class, new Annotation[0]);
        final MapperFailureData retrofitApiFailureDataMapper
                = MapperFailureData.create(apiFailureDataConverter);
        final MapperResult.Creator retrofitApiResultMapperCreator = MapperResult
                .creator(retrofitApiFailureDataMapper);
        final EmptyMapperResult.Creator retrofitApiResultEmptyMapperCreator = EmptyMapperResult
                .creator(retrofitApiFailureDataMapper); */

        // ApiRetrofitImpl.create(api, retrofitApiResultMapperCreator, retrofitApiResultEmptyMapperCreator,
              //  getBaseContext());



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
      //  Single<Response<ApiSecretTokenResponse>> encryptedMaintenance = api.getEncryptedMaintenance(insurance, model, make, year, millage, maintenance);
        // Toast.makeText(this, encryptedMaintenance.toString(), Toast.LENGTH_LONG).show();
        Call<CodeForQRImage> encryptedMaintenance = api.getEncryptedMaintenance(insurance, model, make, year, millage, maintenance);
        encryptedMaintenance.enqueue(new Callback<CodeForQRImage>() {
            @Override
            public void onResponse(Call<CodeForQRImage> call, Response<CodeForQRImage> response) {
              //  Log.i("DEBUG=", response.body().getToken());

               /* if(sessionManager !=null) {
                    sessionManager.saveCustomerSecretToken(response.body().getToken());
                }else{
                    Log.i("DEBUG=", "SESSION MANAGER IS NULL");
                }*/
                if(response.body() != null) {
                    DisplayQrResult(view, response.body().getToken());
                }else{
                    Log.i("DEBUG=", "No se pudo retornar objeto");
                }
            }

            @Override
            public void onFailure(Call<CodeForQRImage> call, Throwable t) {
                Log.i("DEBUG=", "FAIL REQUEST");
            }
        });

    }

    private void DisplayQrResult(View view, String responseQr){
        view.setEnabled(false);

        Intent intent = new Intent(this, QrActivity.class);
        intent.putExtra(QR_CODE_INFORMATION, responseQr);
        startActivityForResult(intent, 0);
    }



    private void testApiIP() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.ipify.org")
                .build();

        IPService service = retrofit.create(IPService.class);

        Call<ApiResponse> call = service.getIp();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.i("DEBUG=", response.body().toString());


            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("DEBUG=", "FAIL REQUEST");
            }
        });
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
