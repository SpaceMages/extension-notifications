#ifndef IPHONE
#define IMPLEMENT_API
#endif

#if defined(HX_WINDOWS) || defined(HX_MACOS) || defined(HX_LINUX)
#define NEKO_COMPATIBLE
#endif

#include <hx/CFFI.h>
#include "Notifications.h"
#include <stdio.h>

//--------------------------------------------------
// Change this to match your extension's ID
//--------------------------------------------------

using namespace notifications;


#ifdef IPHONE

//--------------------------------------------------
// Glues Haxe to native code.
//--------------------------------------------------

void set_icon_badge_number(value number)
{
	setIconBadgeN(val_int(number));
}
DEFINE_PRIM(set_icon_badge_number, 1);

void increase_icon_badge_number(value number)
{
    increaseIconBadgeN(val_int(number));
}
DEFINE_PRIM(increase_icon_badge_number, 1);

void decrease_icon_badge_number(value number)
{
    decreaseIconBadgeN(val_int(number));
}
DEFINE_PRIM(decrease_icon_badge_number, 1);

void cancel_local_notifications()
{
    cancelLocalN();
}
DEFINE_PRIM(cancel_local_notifications, 0);

void schedule_local_notification(value message, value time, value repeat, value title, value action)
{
    scheduleLocalN(val_string(message), val_int(time), val_int(repeat), val_string(title), val_string(action));
}
DEFINE_PRIM(schedule_local_notification, 5);

#endif



//--------------------------------------------------
// IGNORE STUFF BELOW THIS LINE
//--------------------------------------------------

extern "C" void notifications_main()
{	
}
DEFINE_ENTRY_POINT(notifications_main);

extern "C" int notifications_register_prims()
{ 
    return 0; 
}