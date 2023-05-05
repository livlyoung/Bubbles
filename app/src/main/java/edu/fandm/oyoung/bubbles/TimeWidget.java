package edu.fandm.oyoung.bubbles;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Summary of widget problem: By default, widgets can only be updated every
 * 30 minutes due to energy saving reasons, and attempting to update
 * them more frequently will not work. This poses a problem for the
 * widget being designed, as it could cause a delay in updating the time.
 * For example, if the widget is created at 1:25 pm, it cannot update until
 * 1:55 pm, and it will still show "1 PM" between 1:25 and 1:55 pm, rather than
 * "2 PM" after being rounded. There are workarounds available to solve this issue.
 *
 *
 * Sources for solution:
 * https://www.ackee.agency/blog/how-to-reliably-refresh-widgets
 * https://developer.android.com/reference/android/app/AlarmManager
 *
 * Possible solution to problem: We could use the Alarm Manager class.
 * The Android class AlarmManager provides access to system alarm services
 * that allow scheduling an application to run at a specified time in the future.
 * When the alarm triggers, the system broadcasts the Intent associated with it,
 * launching the target application if it's not running.
 * The registered alarms persist when the device is asleep,
 * and can even wake up the device if configured to do so.
 * However, the alarms are cleared if the device is turned off and rebooted.
 *  We can set the update interval to be as frequent as needed,
 *  although frequent updates can have an impact on battery life.
 */


public class TimeWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        //Source: Chat GPT
        // Iterate through all the widgets added to the home screen
        for (int appWidgetId : appWidgetIds) {
            // Create a new RemoteViews object for the widget layout
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            // Get the current time and round it to the nearest hour
            Calendar calendar = Calendar.getInstance();
            int minutes = calendar.get(Calendar.MINUTE);
            if (minutes >= 30) {
                calendar.add(Calendar.HOUR_OF_DAY, 1);
            }
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            // Set the rounded time to the widget layout
            SimpleDateFormat format = new SimpleDateFormat("h a", Locale.getDefault());
            String time = format.format(calendar.getTime());
            views.setTextViewText(R.id.widget_text, time);

            // Update the widget on the home screen
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

