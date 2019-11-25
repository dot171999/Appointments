package com.dot.appointments.custom;

import android.content.res.Resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Tools {

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static String timeToAmPm(String time){ //24H time to AM-PM
        time = time.substring(0,2)+":"+time.substring(2,4);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        try{
            Date date3 = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm aa",Locale.US);
            time = sdf2.format(date3);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return time;
    }

    public static String getWeek(){
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.US);

        Calendar calendar = Calendar.getInstance();
        return dayFormat.format(calendar.getTime());
    }

    public static String getDay(){
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.US);

        Calendar calendar = Calendar.getInstance();
        return dayFormat.format(calendar.getTime());
    }
}
