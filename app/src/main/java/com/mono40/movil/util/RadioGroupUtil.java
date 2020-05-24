package com.mono40.movil.util;

import android.app.Activity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RadioGroupUtil {

  public static void setRadioButtons(RadioGroup radioGroup, String[] groupNames, Activity activity){
    for(int i = 0; i < groupNames.length; i++){
      RadioButton radioButton = new RadioButton(activity);
      radioButton.setId(i);
      radioButton.setText(groupNames[i]);
      radioButton.setPadding(0, 16, 0, 16);
      radioGroup.addView(radioButton);
    }
  }
}
