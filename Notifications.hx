package;

#if cpp
import cpp.Lib;
#elseif neko
import neko.Lib;
#else
import openfl.Lib;
#end

#if android
import openfl.utils.JNI;
#end

class Notifications
{
    #if ios 
    
    private static var setIconBadgeNumber = Lib.load("notifications","set_icon_badge_number",1);
    private static var increaseIconBadgeNumberBy = Lib.load("notifications","increase_icon_badge_number",1);
    private static var decreaseIconBadgeNumberBy = Lib.load("notifications","decrease_icon_badge_number",1);
    private static var cancelLocalNotifications = Lib.load("notifications","cancel_local_notifications",0);
    private static var scheduleLocalNotification = Lib.load("notifications","schedule_local_notification",5);
	
    #end
	
	#if android
	private static var scheduleLocalNotification:Dynamic;
	private static var setIconBadgeNumber:Dynamic;
	private static var increaseIconBadgeNumberBy:Dynamic;
	private static var decreaseIconBadgeNumberBy:Dynamic;
	private static var cancelLocalNotifications:Dynamic;
	
	#end
	
	
    public static function hxSetIconBadgeNumber(number:Int = 0):Void
    {
		#if ios
        setIconBadgeNumber(number);
		#end
		
		#if android
		 if(setIconBadgeNumber == null)
            {
                setIconBadgeNumber = JNI.createStaticMethod("com.byrobin.Notification.NotificationsExtension", "setIconBadge", "(I)V", true);
            }
            var args = new Array<Dynamic>();
            args.push(number);
            setIconBadgeNumber(args);
		#end
    }
    
    public static function hxIncreaseIconBadgeNumberBy(number:Int = 0):Void
    {
		#if ios
        increaseIconBadgeNumberBy(number);
		#end
		
		#if android
		if(increaseIconBadgeNumberBy == null)
            {
                increaseIconBadgeNumberBy = JNI.createStaticMethod("com.byrobin.Notification.NotificationsExtension", "increaseIconBadge", "(I)V", true);
            }
            var args = new Array<Dynamic>();
            args.push(number);
            increaseIconBadgeNumberBy(args);
		#end
    }
    
    public static function hxDecreaseIconBadgeNumberBy(number:Int = 0):Void
    {
		#if ios
        decreaseIconBadgeNumberBy(number);
		#end
		
		#if android
		if(decreaseIconBadgeNumberBy == null)
            {
                decreaseIconBadgeNumberBy = JNI.createStaticMethod("com.byrobin.Notification.NotificationsExtension", "decreaseIconBadge", "(I)V", true);
            }
            var args = new Array<Dynamic>();
            args.push(number);
            decreaseIconBadgeNumberBy(args);
		#end
    }
    
    public static function hxCancelLocalNotifications():Void
    {
		#if ios
        cancelLocalNotifications();
		#end
		
		#if android
		if(cancelLocalNotifications == null)
            {
                cancelLocalNotifications = JNI.createStaticMethod("com.byrobin.Notification.NotificationsExtension", "cancelAllNotification", "()V", true);
            }
            var args = new Array<Dynamic>();
            cancelLocalNotifications();
		#end
    }

    /**
    * Schedules the notification
    * 
    * @param    id         (Android) Unique id of the notification. Existing notifications with same id will be overwritten
    * @param    message    Content of notification
    * @param    days       Days until notification fires
    * @param    hours      Hours until notification fires
    * @param    minutes    Minutes until notification fires
    * @param    seconds    Seconds until notification fires
    * @param    repeat     Interval to repeat this notification
    *                          0 = No repeat
    *                          1 = Every minute
    *                          2 = Every hour
    *                          3 = Every day
    *                          4 = Every week
    *                          5 = Every month
    *                          6 = Every 3 months
    *                          7 = Every year
    * @param    subtext    (Android)
    * @param    ticker     (Android)
    * @param    title      Defaults to App Name if not specified
    * @param    action     (IOS) "Swipe to [action]" text
    */
    public static function hxScheduleLocalNotification(id:Int = 1, message:String = "none", days:Int = 0, hours:Int = 0, minutes:Int = 0, seconds:Int = 0, repeat:Int = 0, subtext:String = "", ticker:String = "", title:String = "", action:String = ""):Void
    {
        seconds = seconds + (minutes*60) + (hours*3600) + (days*86400);
        
        #if ios
        scheduleLocalNotification(message, seconds, repeat, title, action);
        #end
        
        #if android

            if(scheduleLocalNotification == null)
            {
                scheduleLocalNotification = JNI.createStaticMethod("com.byrobin.Notification.NotificationsExtension", "scheduleNotification", "(ILjava/lang/String;IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", true);
            }
            var args = new Array<Dynamic>();
            args.push(id);
            args.push(message);
            args.push(seconds);
            args.push(repeat);
            args.push(subtext);
            args.push(ticker);
            args.push(title);

            scheduleLocalNotification(args);
        #end
    }
   
}
