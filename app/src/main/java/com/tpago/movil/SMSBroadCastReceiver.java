package com.tpago.movil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.tpago.movil.util.StringHelper;

/**
 * Created by solucionesgbh on 7/20/18.
 */

public class SMSBroadCastReceiver extends BroadcastReceiver {
  public static String OTP_IDENTIFIER = "otp";
  public static String[] TPAGO_SMS_IDENTIFIERS = {"tPago", "150"};

  public static String MESSAGE = "message";
  public static String NUMBER = "number";

  private String PDUS_IDENTIFIER = "pdus";

  @Override
  public void onReceive(Context context, Intent intent) {
    // Retrieves a map of extended data from the intent.
    final Bundle bundle = intent.getExtras();
    try {
      if (bundle != null) {
        final Object[] pdusObj = (Object[]) bundle.get(PDUS_IDENTIFIER);
        for (int i = 0; i < pdusObj.length; i++) {

          SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
          String phoneNumber = currentMessage.getDisplayOriginatingAddress();

          String senderNum = phoneNumber;
          String message = currentMessage.getDisplayMessageBody();

          boolean canBroadCast = false;

          for (String identifier: TPAGO_SMS_IDENTIFIERS) {
            if(identifier.toLowerCase().equals(senderNum.toLowerCase())) {
              canBroadCast = true;
            }
          }

          if (canBroadCast) {
            Intent myIntent = new Intent(OTP_IDENTIFIER);
            myIntent.putExtra(MESSAGE, StringHelper.extractOTPcode(message));
            myIntent.putExtra(NUMBER, senderNum);
            LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
          }
        }
      }
    } catch (Exception e) {
      Log.e("SmsReceiver", "Exception smsReceiver" + e);
    }
  }
}
