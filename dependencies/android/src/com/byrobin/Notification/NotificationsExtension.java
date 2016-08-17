package com.byrobin.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.util.Log;

import org.haxe.extension.Extension;

import java.util.Calendar;
import java.util.GregorianCalendar;

import me.leolin.shortcutbadger.ShortcutBadger;


public class NotificationsExtension extends Extension {
    
    public static long repeatLong;
    public static long seconds;
    
    public static final String MY_ACTION = "com.byrobin.Notification.myaction";
    
    static PendingIntent pendingIntent;
    
    public static void scheduleNotification(int id, String message,long seconds,long repeat,String subtext, String ticker, String title)
    {
        setIconBadge(0);
        
        repeatLong = repeat;
        
        SharedPreferences sharedPref= mainContext.getSharedPreferences("com.byrobin.Notification",Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putInt("id", id);
        editor.putString("msg", message);
        editor.putString("subtext", subtext);
        editor.putString("ticker", ticker);
        editor.putString("title", title);
        editor.commit();
        
        // Define a time value of seconds
        Long alertTime = new GregorianCalendar().getTimeInMillis()+seconds*1000;
        
        // Define our intention of executing Receiver
        //Intent alertIntent = new Intent(mainActivity, NCReceiver.class);
        Intent alertIntent = new Intent(MY_ACTION);
        
        // Allows you to schedule for your application to do something at a later date
        // even if it is in he background or isn't active
        AlarmManager alarmManager = (AlarmManager) mainContext.getSystemService(Context.ALARM_SERVICE);
        
        // set() schedules an alarm to trigger
        // Trigger for alertIntent to fire in seconds
        // FLAG_UPDATE_CURRENT : Update the Intent if active
        
        pendingIntent = PendingIntent.getBroadcast(mainActivity, id, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        
        if(repeatLong == 0){
            alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent);;
            
        }else{
            
            Calendar alarmStartTime = Calendar.getInstance();
            alarmStartTime.add(Calendar.MINUTE, 1);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alertTime, getInterval(), pendingIntent);
        }
    }
    
    private static Long getInterval(){
        
        // if(repeatLong == 1){// minute
            
        //     seconds = 60;
            
        // }else if(repeatLong == 2){// hour
            
        //     seconds = 60 * 60;
            
        // }else if(repeatLong == 3){// day
            
        //     seconds = 60 * 60 * 24;
            
        // }else if(repeatLong == 4){// week
            
        //     seconds = 60 * 60 * 24 * 7;
            
        // }else if(repeatLong == 5){// month
            
        //     seconds = 60 * 60 * 24 * 7 * 4;
            
        // }else if(repeatLong == 6){//3 months
            
        //     seconds = 60 * 60 * 24 * 7 * 4 * 3;
            
        // }else if(repeatLong == 7){// year
            
        //     seconds = 60 * 60 * 24 * 7 * 4 * 12;
            
        // }
        
     int milliseconds = 1000;
     long repeatMS = repeatLong * 1 * milliseconds;
     return repeatMS;
     }
    
    public static void cancelAllNotification()
    {
        NotificationManager notificationMR = ((NotificationManager) mainContext.getSystemService(Context.NOTIFICATION_SERVICE));
        notificationMR.cancelAll();
        
        
        AlarmManager alarmManager = (AlarmManager) mainContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
       
    }
    
    public static void setIconBadge(int number)
    {
        int badgeCount = number;
        
        SharedPreferences sharedPref= mainContext.getSharedPreferences("com.byrobin.Notification",Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putInt("badge", badgeCount);
        editor.commit();
        
        ShortcutBadger.applyCount(mainContext.getApplicationContext(), badgeCount);
    }
    
    public static void increaseIconBadge(int number)
    {
        SharedPreferences sharedPref= mainContext.getSharedPreferences("com.byrobin.Notification",Context.MODE_WORLD_READABLE);
        int badgeCount = sharedPref.getInt("badge", 0);
        
        
        int increaseBadgeCount = badgeCount + number;
        ShortcutBadger.applyCount(mainContext.getApplicationContext(), increaseBadgeCount);
        
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putInt("badge", increaseBadgeCount);
        editor.commit();
    }
    
    public static void decreaseIconBadge(int number)
    {
        
        SharedPreferences sharedPref= mainContext.getSharedPreferences("com.byrobin.Notification",Context.MODE_WORLD_READABLE);
        int badgeCount = sharedPref.getInt("badge", 0);
        
        int decreaseBadgeCount = badgeCount -+ number;
        ShortcutBadger.applyCount(mainContext.getApplicationContext(), decreaseBadgeCount);
        
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putInt("badge",decreaseBadgeCount);
        editor.commit();
    }
    

    public void onCreate (Bundle savedInstanceState)
    {
        Log.d("localNotif","onCreate");
        setIconBadge(0);
        super.onCreate(savedInstanceState);
    }
    
    
    public void onStart()
    {
        Log.d("localNotif","onStart");
        setIconBadge(0);
        super.onStart();
    }
    
    
    public void onResume()
    {
        Log.d("localNotif","onResume");
        setIconBadge(0);
        super.onResume();
        
    }
    
    public void onRestart()
    {
        Log.d("localNotif","onRestart");
        setIconBadge(0);
        super.onRestart();
    }
    
    public void onPause()
    {
        Log.d("localNotif","onPause");
        setIconBadge(0);
        super.onPause();
    }
    
    public void onStop()
    {
        Log.d("localNotif","onStop");
   
        super.onStop();
    }
    
}