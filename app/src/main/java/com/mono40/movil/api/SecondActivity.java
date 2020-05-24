package com.mono40.movil.api;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mono40.movil.R;
import com.mono40.movil.app.ui.activity.base.ActivityBase;
import com.mono40.movil.d.ui.main.recipient.index.category.RecipientCategoryFragment;
import com.mono40.movil.dep.widget.TextInput;

import butterknife.BindView;

public class SecondActivity extends ActivityBase {

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

    @Override
    protected int layoutResId() {
        return R.layout.second_check_activity;
    }


    public void finishCheck(View view) {
        Bundle extras = this.getIntent().getExtras();

        String insurance = extras.getString(RecipientCategoryFragment.EXTRA_INSURANCE, "DEFAULT INSURANCE");
        String millage = extras.getString(RecipientCategoryFragment.EXTRA_MILLAGE, "DEFAULT MILLGAGE");
        String year = extras.getString(RecipientCategoryFragment.EXTRA_YEAR, "DEFAULT YEAR");
        String model = extras.getString(RecipientCategoryFragment.EXTRA_MODEL, "DEFAULT MODEL");
        String make = extras.getString(RecipientCategoryFragment.EXTRA_MAKE, "DEFAULT MAKE");

        String message = "Finish " +
                " insurance=" + insurance +
                " millage=" + millage +
                " year=" + year +
                " model=" + model +
                " make=" + make;

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
