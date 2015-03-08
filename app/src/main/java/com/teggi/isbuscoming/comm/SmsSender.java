package com.teggi.isbuscoming.comm;

import android.telephony.SmsManager;

/**
 * Created by Komoliddin on 3/3/2015.
 */
public class SmsSender {
    public final static String DEFAULT_MTA_NUMBER = "511123";

    public static void checkStatus(String busStop){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(DEFAULT_MTA_NUMBER, null, busStop, null, null);
    }
}
