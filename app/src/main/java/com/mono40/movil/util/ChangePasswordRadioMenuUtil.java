package com.mono40.movil.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import com.mono40.movil.R;

public class ChangePasswordRadioMenuUtil {
  public static Dialog createChangePasswordRadioMenuDialog(Activity activity){
    Dialog dialog = new Dialog(activity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.fragment_recover_password_chooser);
    return dialog;
  }
}
