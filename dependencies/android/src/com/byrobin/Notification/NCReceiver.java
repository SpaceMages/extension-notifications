package com.byrobin.Notification;

import android.app.Activity;
import android.support.v4.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.*;

import org.haxe.extension.Extension;

import me.leolin.shortcutbadger.ShortcutBadger;

import android.view.Window;
import android.util.Log;

public class NCReceiver extends BroadcastReceiver {
    
    public Bitmap bm;
    public PendingIntent contentIntent;
    
    // Called when a broadcast is made targeting this class
    @Override
    public void onReceive(Context arg0, Intent arg1)
    {

        SharedPreferences sharedPref= arg0.getSharedPreferences(arg1.getAction() ,Context.MODE_WORLD_READABLE);
        //SharedPreferences sharedPref= arg0.getSharedPreferences("com.byrobin.Notification" ,Context.MODE_WORLD_READABLE);
        int    id           = sharedPref.getInt("id", 1);
        String msg          = sharedPref.getString("msg", "");
        String subtext      = sharedPref.getString("subtext", "");
        String ticker       = sharedPref.getString("ticker", "");
        String title        = sharedPref.getString("title", "");
        String bigIcon      = sharedPref.getString("bigIcon", "");
        String whiteIcon    = sharedPref.getString("whiteIcon", "");
        String bgColor      = sharedPref.getString("bgColor", "");
        boolean soundOff    = sharedPref.getBoolean("soundOff", false);
        boolean vibrateOff  = sharedPref.getBoolean("vibrateOff", false);
        boolean lightsOff   = sharedPref.getBoolean("lightsOff", false);

        createNotification(arg0, arg1, id, msg, subtext, ticker, title, bigIcon, whiteIcon, bgColor, soundOff, vibrateOff, lightsOff);
    }

    private int getNotificationIcon(Context context, String whiteIcon) {

        String pkg = "::APP_PACKAGE::";
        PackageManager pm = context.getPackageManager();
        int iconID = -1;
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= 21); // 21 == android.os.Build.VERSION_CODES.LOLLIPOP

        if(useWhiteIcon){

            try {
                if(whiteIcon != "") {
                    iconID = context.getResources().getIdentifier(whiteIcon , "drawable", context.getPackageName());    
                }
            } catch (Exception e) {

            }

        } else {

            try {
                ApplicationInfo ai = pm.getApplicationInfo(pkg, 0);
                iconID = ai.icon;
            } catch (Exception e) {

            }

        }

        if(iconID == -1) iconID = android.R.drawable.ic_dialog_info;

        return iconID;
    }

    public void createNotification(Context context, Intent intent, int id, String message, String subtext, String ticker, String title, String bigIcon, String whiteIcon, String bgColor, boolean soundOff, boolean vibrateOff, boolean lightsOff)
    {

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String tickerText = "You have a new message from ::APP_TITLE::";
        if(!ticker.isEmpty()) { tickerText = ticker; }

        String contentTitle = "::APP_TITLE::";
        if(!title.isEmpty()) { contentTitle = title; }

        String contentSubtext = subtext;
        String contentText = message;
        
        Extension.assetManager = context.getAssets();

        if(bigIcon != "") {
            try {
                bm = BitmapFactory.decodeStream(Extension.assetManager.open(bigIcon));
            } catch (Exception e) {
                Log.w("Resource list", "Bitmap Exception happened");
                Log.w("Resource list", e.getMessage());
                bm = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_dialog_info);
            }
        }
        
        long when = System.currentTimeMillis();
           

// api 8
        //Intent notificationIntent = new Intent(context, Extension.mainActivity.getClass());
       // PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        
        // PendingIntent contentIntent= PendingIntent.getActivity(context.getApplicationContext(), 0, new Intent(), 0);
        
        // Notification notification = new Notification(iconID, tickerText, when);
        // notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        // notification.defaults |= Notification.DEFAULT_SOUND;
        // notification.defaults |= Notification.DEFAULT_VIBRATE;
        // notification.flags |= Notification.FLAG_AUTO_CANCEL;
    
        // mNotificationManager.notify(1, notification);

// api 11

        // Launch or open application on notification tap
        Intent notificationIntent = null;
        try {
            PackageManager pm = context.getPackageManager();
            if(pm != null) {
                String packageName = context.getPackageName();
                notificationIntent = pm.getLaunchIntentForPackage(packageName);
                notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER); // Should already be set, but just in case
            }
        } catch (Exception e) {
            Log.w("Notification Scheduler", "Failed to get application launch notificationIntent");
            Log.w("Notification Scheduler", e.getMessage());
        }
        
        if(notificationIntent == null) {
            Log.i("Notification Scheduler", "Falling back to empty notificationIntent");
            notificationIntent = new Intent();
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext());

        builder.setAutoCancel(true);
        builder.setContentTitle(contentTitle);               
        builder.setContentText(contentText);
        builder.setSmallIcon(getNotificationIcon(context, whiteIcon));
        if(bm != null) {
            builder.setLargeIcon(bm);
        }
        // 21/23 and up
        if(bgColor != "") {
            builder.setColor( Color.parseColor(bgColor) );
        }
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(false);
        builder.setWhen(when);
        builder.setTicker(tickerText);

        int defaultSettings = 0;
        if(!soundOff)   defaultSettings += NotificationCompat.DEFAULT_SOUND;
        if(!vibrateOff) defaultSettings += NotificationCompat.DEFAULT_VIBRATE;
        if(!lightsOff)  defaultSettings += NotificationCompat.DEFAULT_LIGHTS;
        builder.setDefaults( defaultSettings );

        if(!contentSubtext.isEmpty()) {
            builder.setSubText(contentSubtext);   //API level 16
        }
        builder.build();

        Notification notification = builder.getNotification();
        mNotificationManager.notify(id, notification);


        //set badge number
        
        SharedPreferences sharedPref= context.getSharedPreferences(intent.getAction(),Context.MODE_WORLD_READABLE);
        int badgeCount = sharedPref.getInt("badge", 0);
        
        badgeCount++;
 
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putInt("badge",badgeCount);
        editor.commit();
        
        ShortcutBadger.applyCount(context.getApplicationContext(), badgeCount);

    }
    
}
