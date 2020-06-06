package com.mono40.movil.d.ui.qr;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mono40.movil.R;
import com.mono40.movil.ServiceInformation.CarServiceInformation;
import com.mono40.movil.ServiceInformation.Maintenance;
import com.mono40.movil.app.ui.Label;
import com.mono40.movil.app.ui.main.transaction.disburse.index.FragmentDisburseIndex;
import com.mono40.movil.app.ui.permission.PermissionHelper;
import com.mono40.movil.d.ui.main.recipient.addition.NonAffiliatedPhoneNumberRecipientAdditionActivityBase;
import com.mono40.movil.d.ui.main.recipient.index.category.Category;
import com.mono40.movil.d.ui.main.recipient.index.category.RecipientCategoryFragment;
import com.mono40.movil.dep.App;
import com.mono40.movil.dep.widget.TextInput;
import com.mono40.movil.util.ObjectHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ScannedQrFragment extends Fragment {

    @BindView(R.id.label3)
    com.mono40.movil.dep.widget.Label modelo;
    @BindView(R.id.label4)
    com.mono40.movil.dep.widget.Label seguro;
    @BindView(R.id.label5)
    com.mono40.movil.dep.widget.Label marca;
    @BindView(R.id.label6)
    com.mono40.movil.dep.widget.Label año;
    @BindView(R.id.label7)
    com.mono40.movil.dep.widget.Label millaje;

    @BindView(R.id.txtSeguro)
    TextInput seguroTextInput;

    @BindView(R.id.txtMarca)
    TextInput marcaTextInput;

    @BindView(R.id.txtModelo)
    TextInput modeloTextInput;

    @BindView(R.id.txtAno)
    TextInput anoTextInput;

    @BindView(R.id.txtMillaje)
    TextInput millajeTextInput;

    @BindView(R.id.qradioButton)
    RadioButton oilChangeRadiobutton;

    @BindView(R.id.qradioButton2)
    RadioButton oilFilterChangeRadiobutton;

    @BindView(R.id.qradioButton3)
    RadioButton transmissionFluidRadiobutton;

    @BindView(R.id.qradioButton4)
    RadioButton brakeOilRadioButton;

    @BindView(R.id.qradioButton5)
    RadioButton oilHydraulicRadiobutton;

    @BindView(R.id.qradioButton6)
    RadioButton oilAirConditionRadiobutton;

    @BindView(R.id.qradioButton7)
    RadioButton waterWiperRadiobutton;

    @BindView(R.id.qradioButton8)
    RadioButton batteryWaterRadiobutton;

    @BindView(R.id.qradioButton9)
    RadioButton radiatorHosesRadiobutton;

    @BindView(R.id.qradioButton10)
    RadioButton warmerHosesRadioButton;

    @BindView(R.id.qradioButton11)
    RadioButton airConditionerHoseRadioButton;

    @BindView(R.id.qradioButton12)
    RadioButton airConditionerFilterRadioButton;

    @BindView(R.id.qradioButton13)
    RadioButton tireAirPressureRadioButton;

    @BindView(R.id.qradioButton14)
    RadioButton tiresUseMillageRadiobutton;

    @BindView(R.id.qradioButton15)
    RadioButton wiperWindsShieldRadiobutton;

    @BindView(R.id.qradioButton16)
    RadioButton frontLightsRadiobutton;

    @BindView(R.id.qradioButton17)
    RadioButton seatBeltRadiobutton;

    @BindView(R.id.qradioButton18)
    RadioButton breakRadiobutton;

    private Unbinder unbinder;

    private Maintenance maintenance;
    private CarServiceInformation carServiceInformation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.scanned_qr_fragment, container, false);
        ButterKnife.bind(this, view);
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        String insurance = bundle.getString("insurance");
        String make = bundle.getString("make");
        String model = bundle.getString("model");
        String miles = bundle.getString("miles");
        String year = bundle.getString("year");
        Maintenance maintenance = bundle.getParcelable("maintenance");

        Log.d("com.cryptoqr.mobile", "Parcelable = " + String.valueOf(maintenance.isOilChange()));
        seguroTextInput.setText(insurance);
        marcaTextInput.setText(make);
        modeloTextInput.setText(model);
        anoTextInput.setText(year);
        millajeTextInput.setText(miles);

        setScreeFromMaintenance(maintenance);
            return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setScreeFromMaintenance(Maintenance maintenance){
        oilChangeRadiobutton.setChecked(maintenance.isOilChange());
        oilFilterChangeRadiobutton.setChecked(maintenance.isOilFilterChange());
        transmissionFluidRadiobutton.setChecked(maintenance.isTransmissionFluidChange());
        oilHydraulicRadiobutton.setChecked(maintenance.isSteeringFluidChange());
        oilAirConditionRadiobutton.setChecked(maintenance.isAirCondHosesCheck());
        waterWiperRadiobutton.setChecked(maintenance.isWipeWaterCheck());
        batteryWaterRadiobutton.setChecked(maintenance.isBatteryWaterChange());
        radiatorHosesRadiobutton.setChecked(maintenance.isRadiatorHosesCheck());

        warmerHosesRadioButton.setChecked(maintenance.isHeaterHosesCheck());
        airConditionerHoseRadioButton.setChecked(maintenance.isAirCondHosesCheck());
        airConditionerFilterRadioButton.setChecked(maintenance.isAirFilterChange());
        tireAirPressureRadioButton.setChecked(maintenance.isTirePressureCheck());
        tiresUseMillageRadiobutton.setChecked(maintenance.isTireWearCheck());
        wiperWindsShieldRadiobutton.setChecked(maintenance.isWipersCheck());
        frontLightsRadiobutton.setChecked(maintenance.isHeadLampAlignmentCheck());
        seatBeltRadiobutton.setChecked(maintenance.isSeatBeltCheck());
        breakRadiobutton.setChecked(maintenance.isParkingBreakCheck());
        brakeOilRadioButton.setChecked(maintenance.isBrakeFluidChange());

    }



}
