package com.teggi.isbuscoming.helpers;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Komoliddin on 3/7/2015.
 */
public class TimeHelper {
    public static String currTime(){
        Calendar calendar = new GregorianCalendar();
        Date trialTime = new Date();
        calendar.setTime(trialTime);

        String currTime = "";
        currTime += calendar.get(Calendar.HOUR);
        currTime += ":" + calendar.get(Calendar.MINUTE);
        currTime += ":" + calendar.get(Calendar.SECOND);
        currTime += " " + (calendar.get(Calendar.AM_PM)==1 ? "AM" : "PM");
        currTime += " " + calendar.get(Calendar.MONTH);
        currTime += "/" + calendar.get(Calendar.DATE);
        currTime += "/" + calendar.get(Calendar.YEAR);

        return currTime;
    }
}
