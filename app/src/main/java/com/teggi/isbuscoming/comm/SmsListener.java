package com.teggi.isbuscoming.comm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.teggi.isbuscoming.R;
import com.teggi.isbuscoming.android.BusStopsActivity;

/**
 * Created by Komoliddin on 3/7/2015.
 */
public class SmsListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage[] smsMessages = null;
            String sms_from;
            if (bundle != null){
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    smsMessages = new SmsMessage[pdus.length];
                    for(int i=0; i<smsMessages.length; i++){
                        smsMessages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        sms_from = smsMessages[i].getOriginatingAddress();

                        if (sms_from.equals(SmsSender.DEFAULT_MTA_NUMBER)) {
                            Toast.makeText(context, R.string.statusReceived, Toast.LENGTH_SHORT).show();
                            String sms_body = smsMessages[i].getMessageBody();
                            BusStopsActivity.displayStatus(sms_body);
                            abortBroadcast(); //doesn't seem to prevent hangouts
                        }
                    }
                } catch(Exception e){
                            Log.d("Exception caught", e.getMessage());
                }
            }
        }
    }
}
